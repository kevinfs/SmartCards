



EXE_SUFFIX=

ARCHS=   
CFLAGS+= $(ARCHS)
CFLAGS += -mmacosx-version-min=10.6
CFLAGS_MONGOOSE=  -I./mongoose -pthread -g 
LIBS+=	 $(ARCHS)




CFLAGS += $(INCLUDES) -O -Wall -std=gnu99


all: main

main: main.o main.o 
	$(CC) $(CFLAGS) -o main$(EXE_SUFFIX) main.o  $(LIBS) -lopencv_core -lopencv_highgui -lopencv_imgproc -lopencv_videoio -lopencv_imgcodecs


.c.o:
	$(CC) $(CFLAGS) -c $*.c -o $*.o -lopencv_core -lopencv_highgui -lopencv_imgproc -lopencv_videoio -lopencv_imgcodecs


clean:
	rm -f $(OBJ) main main.exe *.o *.a

