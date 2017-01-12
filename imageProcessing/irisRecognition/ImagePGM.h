#include "Image.h"

#include<cmath>
#include<math.h>

using namespace std;

class ImagePGM: public Image {

public:
    
    /************ Constructeur par défaut ***************/
	ImagePGM();
    
    /************** Constructeur avec la longueur et la largeur comme paramètre *********/
	ImagePGM(int x, int y);
    

    /************** Constructeur avec la longueur, la largeur et un tableau de byte comme paramètre *********/
    ImagePGM(int x, int y, byte *tab);
	
     /**********************************************************************/
    /*********** Trouver l'indice du pixel dans le nouveau tableau ********/
   /**********************************************************************/
    int findPixel(int x, int y);
	  /**********************************************************************/
     /*********** Pour définir la taille du tableau en fonction*************/
    /********************** du type de fichier PPM ou PGM *****************/
   /**********************************************************************/
    int arraySize();

    void barycentre( int *x, int *y);

    /**********************************************************************/
   /*********** Méthode de sobel avec le seuil en paramètre  *************/
  /**********************************************************************/
	ImagePGM sobel(int sobelLevel, double ** gDirection);

  void modifyImg( int x, int y, int color) ;
  int getPixel( int x, int y );

  /*******************************************************************/
 /************************Histogramme de l'image ********************/
/*******************************************************************/

  int* histogramme();

  int countPixels();


};
