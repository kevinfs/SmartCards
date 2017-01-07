#include"ImagePGM.h"
#include<string>

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


void ImagePGM::barycentre( int *x, int *y) {
    int sumX = 0, sumY = 0;
    int barX = 0, barY = 0;	

    for (int i = 1; i <= _width - 1; i++) {
		for (int j = 1; j <= _height - 1; j++) {
            if (_array[findPixel(i, j )] == 255) {
                barX += i;
                barY += j;

                sumX++;
                sumY++;
            }
        }
    }

    *x = barX / sumX;
    *y = barY / sumY;
    // _array[findPixel(*x, *y )] =0;//this line print the barycentre in the picture

}

void ImagePGM::modifyImg( int x, int y, int color) {
  
    _array[findPixel(x, y )] = color;//this line print  in the picture

}

ImagePGM ImagePGM::sobel(int sobelLevel, double ** gDirection) {
    
    
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
	for (int i = 1; i <= _width - 1; i++) {
		for (int j = 1; j <= _height - 1; j++) {
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
            
            // calcul de la valeur du gradiant : norme 
            g = sqrt(gradiantX * gradiantX + gradiantY * gradiantY);
            // direction du gradient
    
    /************************************************************************/
   /********** Comparaison des valeurs des pixel avec le seuil *************/
  /************************************************************************/
            
            if (g > sobelLevel) {// CONTOURS
            	tmp = 255; // On detecte les contours en attribuant une couleur blanche

            	gDirection[i][j] = atan((double)gradiantY/(double)gradiantX)*180/M_PI; //conversion radian to degree
            // printf("%d\n", *gDirection );

            } else {
            	tmp = 0; // si pas de contour on met la couleur à noir
            	gDirection[i][j] = 0.0;

            }
           
        
            
            sobelArray[findPixel(i, j)] = tmp;
        }
	}
	// retourner sobelArray, gradiantX, gradiantY  !!!!
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

