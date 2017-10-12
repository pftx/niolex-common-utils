CXX = g++
CXXFLAGS = -Wall -std=c++11 -O3

.PHONY : all clean

all : list_test

list_test : list_test.o list.o string.o
	$(CXX) $(CXXFLAGS) -o $@  $^

list_test.o : list.h

list.o : list.h

string.o : string.h

clean :
	rm -f *.o
	rm -f *.exe