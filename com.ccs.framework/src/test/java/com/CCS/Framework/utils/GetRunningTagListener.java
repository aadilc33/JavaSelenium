package com.CCS.Framework.utils;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestSourceRead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("ALL")
public class GetRunningTagListener implements ConcurrentEventListener {
    private final String tagFilter;
    private final Features features = new Features();
    private static final Logger logger = LoggerFactory.getLogger(GetRunningTagListener.class);


    public GetRunningTagListener() {
        this.tagFilter = System.getProperty("cucumber.filter.tags", "");
        logger.info("Active tag filter: {}", tagFilter);
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, this::handleTestSourceRead);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::generateExecutionReport);
    }

    private void handleTestSourceRead(TestSourceRead event) {
        String featurePath = event.getUri().toString();
        String featureName = featurePath.substring(featurePath.lastIndexOf('/') + 1);

        FeatureExecution feature = new FeatureExecution(featureName);
        feature.getFeatureTags().addAll(parseTagsBeforeFeature(event.getSource()));

        List<ScenarioData> scenarios = parseScenarios(event.getSource());
        for (ScenarioData scenario : scenarios) {
            feature.getScenarios().add(new ScenarioExecution(featureName, scenario.name, scenario.tags));
        }
        features.addFeature(feature);
    }

    private Set<String> parseTagsBeforeFeature(String content) {
        Set<String> tags = new HashSet<>();
        String[] lines = content.split("\n");

        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("Feature:")) break;
            if (line.startsWith("@")) {
                Collections.addAll(tags, line.split("\\s+"));
            }
        }
        return tags;
    }

    private List<ScenarioData> parseScenarios(String content) {
        List<ScenarioData> scenarios = new ArrayList<>();
        String[] parts = content.split("Scenario(?: Outline)?:");

        for (int i = 1; i < parts.length; i++) {
            String scenarioContent = parts[i];
            String scenarioName = scenarioContent.split("\n")[0].trim();
            Set<String> tags = new HashSet<>();

            Matcher matcher = Pattern.compile("@\\w+").matcher(scenarioContent);
            while (matcher.find()) {
                tags.add(matcher.group());
            }

            scenarios.add(new ScenarioData(scenarioName, tags));
        }
        return scenarios;
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        String featureName = event.getTestCase().getUri().toString();
        featureName = featureName.substring(featureName.lastIndexOf('/') + 1);
        String scenarioName = event.getTestCase().getName();

        ScenarioExecution scenario = features.findScenario(featureName, scenarioName);
        if (scenario != null) {
            scenario.setDidRun(true);
        }
    }

    private void generateExecutionReport(TestRunFinished event) {
        logger.info("\n=== SCENARIO EXECUTION REPORT ===");
        logger.info("Tag filter applied: {}", tagFilter);

        List<ScenarioExecution> executedScenarios = new ArrayList<>();
        List<ScenarioExecution> filteredScenarios = new ArrayList<>();

        for (FeatureExecution feature : features.getFeatures()) {
            for (ScenarioExecution scenario : feature.getScenarios()) {
                Set<String> allTags = new HashSet<>(feature.getFeatureTags());
                allTags.addAll(scenario.getTags());

                boolean shouldRun = shouldRunScenario(allTags);
                scenario.setShouldRun(shouldRun);

                if (scenario.isDidRun()) {
                    executedScenarios.add(scenario);
                } else if (shouldRun) {
                    filteredScenarios.add(scenario);
                }
            }
        }

        printResults(executedScenarios, filteredScenarios);
    }

    private boolean shouldRunScenario(Set<String> tags) {
        if (tagFilter.isEmpty()) {
            return true;
        }
        String[] parts = tagFilter.split("\\s+and\\s+|\\s+or\\s+|\\s+not\\s+");
        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("@")) {
                if (part.startsWith("not @")) {
                    String tag = part.substring(5);
                    if (tags.contains(tag)) {
                        return false;
                    }
                } else {
                    if (!tags.contains(part)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void printResults(List<ScenarioExecution> executed, List<ScenarioExecution> filtered) {
        logger.info("\nEXECUTED SCENARIOS ({}):", executed.size());
        executed.forEach(s -> logger.info("  {} > {} (Tags: {})",
                s.getFeatureName(), s.getScenarioName(), s.getTags()));

        logger.info("\nFILTERED SCENARIOS ({}):", filtered.size());
        filtered.forEach(s -> logger.info("  {} > {} (Tags: {})",
                s.getFeatureName(), s.getScenarioName(), s.getTags()));

        printCsvResults(executed, filtered);
    }

    private void printCsvResults(List<ScenarioExecution> executed, List<ScenarioExecution> filtered) {
        System.out.println("\nCSV FORMAT:");
        System.out.println("Status,Feature,Scenario,Tags");

        executed.forEach(s -> System.out.printf("EXECUTED,%s,%s,\"%s\"%n",
                s.getFeatureName(), s.getScenarioName(), String.join(",", s.getTags())));

        filtered.forEach(s -> System.out.printf("FILTERED,%s,%s,\"%s\"%n",
                s.getFeatureName(), s.getScenarioName(), String.join(",", s.getTags())));
    }

    // Data classes
    private static class Features {
        private final List<FeatureExecution> features = new ArrayList<>();

        public void addFeature(FeatureExecution feature) {
            features.add(feature);
        }

        public List<FeatureExecution> getFeatures() {
            return features;
        }

        public ScenarioExecution findScenario(String featureName, String scenarioName) {
            for (FeatureExecution feature : features) {
                if (feature.getName().equals(featureName)) {
                    for (ScenarioExecution scenario : feature.getScenarios()) {
                        if (scenario.getScenarioName().equals(scenarioName)) {
                            return scenario;
                        }
                    }
                }
            }
            return null;
        }
    }

    private static class FeatureExecution {
        private final String name;
        private final Set<String> featureTags = new HashSet<>();
        private final List<ScenarioExecution> scenarios = new ArrayList<>();

        public FeatureExecution(String name) {
            this.name = name;
        }

        public String getName() { return name; }
        public Set<String> getFeatureTags() { return featureTags; }
        public List<ScenarioExecution> getScenarios() { return scenarios; }
    }

    private static class ScenarioData {
        final String name;
        final Set<String> tags;

        ScenarioData(String name, Set<String> tags) {
            this.name = name;
            this.tags = Collections.unmodifiableSet(new HashSet<>(tags));
        }
    }

    private static class ScenarioExecution {
        private final String featureName;
        private final String scenarioName;
        private final Set<String> tags;
        private boolean shouldRun = false;
        private boolean didRun = false;

        ScenarioExecution(String featureName, String scenarioName, Set<String> tags) {
            this.featureName = featureName;
            this.scenarioName = scenarioName;
            this.tags = new HashSet<>(tags);
        }

        public String getFeatureName() { return featureName; }
        public String getScenarioName() { return scenarioName; }
        public Set<String> getTags() { return tags; }
        public boolean isShouldRun() { return shouldRun; }
        public void setShouldRun(boolean shouldRun) { this.shouldRun = shouldRun; }
        public boolean isDidRun() { return didRun; }
        public void setDidRun(boolean didRun) { this.didRun = didRun; }
    }
}