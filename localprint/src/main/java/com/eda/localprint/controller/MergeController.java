package com.eda.localprint.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eda.localprint.service.MergeService;


@RestController
public class MergeController {
	
	@Autowired
	private MergeService mergeService;

	@RequestMapping("/merge")
	public List<String> merge(@RequestParam  String businessnum,
			@RequestParam List<String> pathList) {
		return mergeService.merge(businessnum, pathList);
	}


}
