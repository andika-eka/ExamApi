package com.technicaltale.examapi;

import org.springframework.boot.SpringApplication;

import com.technicaltale.examapi.ExamApiApplication;

public class TestExamApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(ExamApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
