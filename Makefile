SOURCES = $(wildcard *.java)
JC = javac

run:
	@echo Building...
	@$(JC) $(SOURCES)
	java Main

clean:
	rm *.class