all :  iris 

libraries: libiris.a libiris.so 

Image.o : Image.h Image.C
	c++ -c Image.C -fPIC

ImagePGM.o : ImagePGM.h ImagePGM.C
	c++ -c ImagePGM.C -fPIC

ImagePPM.o : ImagePPM.h ImagePPM.C
	c++ -c ImagePPM.C -fPIC

libiris.so :  Image.o ImagePGM.o ImagePPM.o
	c++ -shared -o libiris.so  Image.o ImagePGM.o ImagePPM.o

libiris.a : Image.o ImagePGM.o ImagePPM.o
	ar cr libiris.a Image.o ImagePGM.o ImagePPM.o


generalizedHoughTransform.o :   generalizedHoughTransform.C ImagePPM.h
	c++ -c generalizedHoughTransform.C 

generalizedHoughTransform : generalizedHoughTransform.o libiris.so
	 c++ -fPIC generalizedHoughTransform.o -L. -liris -o generalizedHoughTransform 


iris.o :   iris.C generalizedHoughTransform.h
	c++ -c iris.C 

iris : iris.o libiris.so generalizedHoughTransform.o 
	 c++ -fPIC iris.o generalizedHoughTransform.o -L. -liris -o iris 

clean :
	rm -f *.o  iris  *~
	rm -f *.so  iris *~
	rm -f *.a  iris *~
