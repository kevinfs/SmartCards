all :  histogram

libraries: libhistogram.a libhistogram.so 

Image.o : Image.h Image.C
	c++ -c Image.C -fPIC

ImagePGM.o : ImagePGM.h ImagePGM.C
	c++ -c ImagePGM.C -fPIC

ImagePPM.o : ImagePPM.h ImagePPM.C
	c++ -c ImagePPM.C -fPIC

libhistogram.so :  Image.o ImagePGM.o ImagePPM.o
	c++ -shared -o libhistogram.so  Image.o ImagePGM.o ImagePPM.o

libhistogram.a : Image.o ImagePGM.o ImagePPM.o
	ar cr libhistogram.a Image.o ImagePGM.o ImagePPM.o


generalizedHoughTransform.o :   generalizedHoughTransform.C ImagePPM.h
	c++ -c generalizedHoughTransform.C 

generalizedHoughTransform : generalizedHoughTransform.o libhistogram.so
	 c++ -fPIC generalizedHoughTransform.o -L. -lhistogram -o generalizedHoughTransform 

histogram.o :   histogram.C generalizedHoughTransform.h
	c++ -c histogram.C 

histogram : histogram.o libhistogram.so generalizedHoughTransform.o 
	 c++ -fPIC histogram.o generalizedHoughTransform.o  -L. -lhistogram -o histogram 

clean :
	rm -f *.o  histogram *~
	rm -f *.so  histogram *~
	rm -f *.a  histogram *~
