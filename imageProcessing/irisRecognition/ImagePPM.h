#include "ImagePGM.h"

using namespace std;


  /*******************************************************************************************************/
 /************* classe  image PPM contenant ************************************************************/
/*****************************************************************************************************/
class ImagePPM: public Image {

public:

	struct histoPixel {
  		int red;
	  	int green;
	  	int blue;
	} ;
	
    /************ Constructeur par défaut ***************/
	ImagePPM();

	/************** Constructeur avec la longueur et la largeur comme paramètre *********/
	ImagePPM(int x, int y);
	
    /******  Calcul de la taille du tableau ***************/
    int arraySize();
	
    
    /********** redifinition de la méthode findPixel(int x, int y) **************/
    int findPixel(int x, int y);
    
	/*************   Niveau de gris ***************/
	ImagePGM grayscale();

	/**** histogramme Rouge *****/
	int *histogrammeR();

	/**** histogramme Couleur *****/
	histoPixel *histogrammeRGB();
	void modifyImg( int x, int y, int r, int g, int b) ;


	



};



