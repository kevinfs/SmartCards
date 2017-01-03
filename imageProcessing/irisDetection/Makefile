all: irisDetection.o
	g++ $(shell pkg-config --cflags --libs opencv) -o irisDetection irisDetection.o

irisDetection.o: irisDetection.cpp
	g++ -c irisDetection.cpp

clean:
	rm *.o irisDetection