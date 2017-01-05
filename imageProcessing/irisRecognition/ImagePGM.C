#include"ImagePGM.h"
#include<string>
#include<cmath>

using namespace std;

ImagePGM::ImagePGM() {
	_type = "P5";
}

ImagePGM::ImagePGM(int x, int y) :
		Image(x, y) {
	_type = "P5";
}

ImagePGM::ImagePGM(int x, int y, byte *tab) :
		Image(x, y) {
	_type = "P5";
	_array = new byte[_size];
	for (int i = 0; i < _size; i++)
		_array[i] = tab[i];
}

/***********Récupération de l'indice à partir des coordonnées (x,y)  ********************************/
int ImagePGM::findPixel(int x, int y) {

	return (y * _width + x);
}

int ImagePGM::arraySize() {
	return _size;
}
  /*************************************************************************************************/
 /*********************** Implémentation du filtre de Sobel ***************************************/
/*************************************************************************************************/

ImagePGM ImagePGM::sobel(int sobelLevel) {
    
    
    /************************************************************************/
    /********** Initialisation des gradiants sur les deux axes *************/
    /**********************************************************************/
	int gradiantX = 0, gradiantY = 0;
	int g;
    
    /**************************************************************************/
   /********************* initiation de la matrice de sobel  *****************/
  /**************************************************************************/
    int sobelx[3][3] = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
    int sobely[3][3] = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
	
    byte sobelArray[_size];
	byte tmp;
	// On commence l'analyse de l'image à partir du pixel(1,1) 
	for (int i = 1; i <= _width - 2; i++) {
		for (int j = 1; j <= _height - 2; j++) {
			// On mutliplie colonne par ligne et en evitant les points qui sont à zero pour
			// Ne pas surcharger le code
			gradiantX = (sobelx[0][0] * _array[findPixel(i - 1, j - 1)]
					+ sobelx[0][1] * _array[findPixel(i, j - 1)]
					+ sobelx[0][2] * _array[findPixel(i + 1, j - 1)]
					+ sobelx[2][0] * _array[findPixel(i - 1, j + 1)]
					+ sobelx[2][1] * _array[findPixel(i, j + 1)]
					+ sobelx[2][2] * _array[findPixel(i + 1, j + 1)]);

			// On mutliplie colonne par ligne et en evitant les points qui sont à zero pour
			// Ne pas surcharger le code
			gradiantY = (sobely[0][0] * _array[findPixel(i - 1, j - 1)]
					+ sobely[0][2] * _array[findPixel(i + 1, j - 1)]
					+ sobely[1][0] * _array[findPixel(i - 1, j)]
					+ sobely[1][2] * _array[findPixel(i + 1, j)]
					+ sobely[2][0] * _array[findPixel(i - 1, j + 1)]
					+ sobely[2][2] * _array[findPixel(i + 1, j + 1)]);
            
            // calcul de la valeur du gradiant
            g = sqrt(gradiantX * gradiantX + gradiantY * gradiantY);
    
    /************************************************************************/
   /********** Comparaison des valeurs des pixel avec le seuil *************/
  /************************************************************************/
            
            if (g > sobelLevel) {
            	tmp = 255; // On detecte les contours en attribuant une couleur blanche
            } else {
            	tmp = 0; // si pas de contour on met la couleur à noir
            }
           
        
            
            sobelArray[findPixel(i, j)] = tmp;
        }
	}
	ImagePGM img(_width, _height, sobelArray);
	return img;
}

int* ImagePGM::histogramme(){

	int* histo = new int[256] ;
	
	for (int i = 0; i < 256 ; i++)
		histo[i] = 0;

	for (int j = 0 ; j < arraySize() ; j++ ){
		
		histo[_array[j]]++; 
	}

	return histo ;
}


int ImagePGM::countPixels(){

	return histogramme()[255];
}

