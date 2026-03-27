FORMATTER = tools/google-java-format.jar

format:
	java -jar $(FORMATTER) --replace $(shell find . -name "*.java")
