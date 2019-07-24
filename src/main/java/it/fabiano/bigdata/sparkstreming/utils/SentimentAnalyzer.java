package it.fabiano.bigdata.sparkstreming.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.google.common.base.Preconditions;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class SentimentAnalyzer {

    private static StanfordCoreNLP pipeline = null;

    public static StanfordCoreNLP getPipelineInstance() {
        if (pipeline == null) {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
            pipeline = new StanfordCoreNLP(props);
        }

        return pipeline;
    }

    public static int calculateWeightedSentimentScore(String text) {
        Annotation annotation = getPipelineInstance().process(text);

        List<Double> sentiments = new ArrayList<Double>();
        List<Integer> sizes = new ArrayList<Integer>();

        int longest = 0;
        int mainSentiment = 0;

        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

        // Sentiment analysis for every sentence
        for (CoreMap sentence : sentences) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);

            String partText = sentence.toString();
            if (partText.length() > longest) {
                mainSentiment = sentiment;
                longest = partText.length();
            }

            sentiments.add(sentiment + 0.0); // convert int to double
            sizes.add(partText.length());
        }

        // Calculate main, avg and weighted sentiment score
       // double avgSentiment = sentiments.isEmpty()? -1 : sentiments.stream().mapToDouble(Double::doubleValue).sum() / sentiments.size();
        double weightedSentiment = sentiments.isEmpty()? -1: getProduct(sentiments, sizes) / sizes.stream().mapToInt(Integer::intValue).sum();
        mainSentiment = sentiments.isEmpty() ? -1: mainSentiment;

        //Debug mode
        //System.out.println("debug: sentences size: " + sentences.size() + "main " + mainSentiment + " avg " + avgSentiment + " weighted " + weightedSentiment);

        return normalizeCoreNLPSentiment(weightedSentiment);
    }
    
    public static String getSimplifiedSentiment(String text) {
    	return normalizeCoreNLPSentimentValue(calculateWeightedSentimentScore(text));
    }

    private static double getProduct(List<Double> sentiments, List<Integer> sizes) {
        Preconditions.checkState(sentiments.size() == sizes.size());

        int size = sentiments.size();
        double result = 0;

        for (int i = 0; i < size; i++) {
            result += sentiments.get(i) * sizes.get(i);
        }

        return result;
    }


    /**
     * 0 -> very negative
     * 1 -> negative
     * 2 -> neutral
     * 3 -> positive
     * 4 -> very positive
     *
     * @param score
     * @return
     */
    private static int normalizeCoreNLPSentiment(double score) {
        if (score <= 0.0) {
            return -999;
        }

        if (score < 1.0) {
            return 0;
        }

        if (score < 2.0) {
            return 1;
        }

        if (score < 3.0) {
            return 2;
        }

        if (score < 4.0) {
            return 3;
        }

        if (score < 5.0) {
            return 4;
        }

        return -999; // can not find the sentiment, give default value
    }
    
    /**
     * 0 -> very negative
     * 1 -> negative
     * 2 -> neutral
     * 3 -> positive
     * 4 -> very positive
     *
     * @param score
     * @return
     */
    private static String normalizeCoreNLPSentimentValue(int score) {
    	
    	if(score==0)
    		return "VERY_NEGATIVE";
    	else if (score==1)
    		return "NEGATIVE";
    	else if (score==2)
    		return "NEUTRAL";
    	else if (score==3)
    		return "POSITIVE";
    	else if (score==4)
    		return "VERY_POSITIVE";
    	else 
    		return "NA";
    	
    }
       
}
