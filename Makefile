SOURCES = $(wildcard *.java)
JC = javac

run:
	@echo Creating $@...
	@$(JC) $(SOURCES)
	java Main

clean:
	rm *.class