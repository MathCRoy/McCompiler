FORMATTER = tools/google-java-format.jar

format:
	java -jar $(FORMATTER) --replace $(shell find src -name "*.java")

run:
	mvn compile -q && mvn exec:java -Dexec.mainClass="Main" -q
