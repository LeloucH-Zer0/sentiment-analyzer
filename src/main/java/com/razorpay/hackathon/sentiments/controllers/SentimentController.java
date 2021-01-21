package com.razorpay.hackathon.sentiments.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.hackathon.sentiments.dto.SentimentRequestDto;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

@RestController
@RequestMapping(path = "/sentiment")
public class SentimentController {

	private static Map<String, Integer> sentimentStringMap = new HashMap<>();

	@PostConstruct
	public static void initMap() {
		sentimentStringMap.put("Very negative", 0);
		sentimentStringMap.put("Negative", 1);
		sentimentStringMap.put("Neutral", 2);
		sentimentStringMap.put("Positive", 3);
		sentimentStringMap.put("Very positive", 4);
	}

	@PostMapping
	public ResponseEntity<Integer> getSentiment(@RequestBody SentimentRequestDto request) {
		Properties props = new Properties();

		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		CoreDocument exampleDocument = new CoreDocument(request.getText());
		pipeline.annotate(exampleDocument);
		return new ResponseEntity<Integer>(sentimentStringMap.get(exampleDocument.sentences().get(0).sentiment()),
				HttpStatus.OK);
	}

}
