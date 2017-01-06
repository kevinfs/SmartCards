#include<fstream>
#include<iostream>
#include<string.h>
#include<stdlib.h>
#include<ctype.h>
#include<string>
#include"Image.h"

  /********************************************************************************************/
 /******************************* Implémentation du constructeur  ****************************/
/********************************************************************************************/
Image::Image(int x, int y) {
	_width = x; // largeur
	_height = y; // longueur
	_size = x * y; // taille de l'image 
	_max_color = 255; // Couleur maximal par défaut
	_array = new byte[_size];// Tableau contenant le corp de l'image
    //Initialisation des valeurs du tableau à zero
	for (int i = 0; i < _size; i++) {
		_array[i] = 0;
	}
}
  /********************************************************************************************/
 /*********** Méthode pour la lecture du fichier binaire  ****************/
/********************************************************************************************/

char *getImageInformations(FILE *image, char *container) {
	
    // conteneur des chaines de caractère (utilisation d 'un pointeur
    char *ch;
	int c;
    // initialisation des variables
	c = 0;
	ch = container;
	
  /***********************************************************/
 /********************** parsing du fichier *****************/
/***********************************************************/
    while (!feof(image)) {
		*ch = fgetc(image); /*** Récupération du premier caractère de la ligne */
		switch (c) {
                
		case 0:
            // Si on a '#' on fait un retour à la ligne
            // # pour ne pas stocker les commentaires
			if (*ch == 35)
				c = 1;
			if (isalnum(*ch))
				c = 2, ch++;
			break;
            // Si on a un retour à la ligne
		case 1:
			if (*ch == 10)
				c = 0;
			break;
		
        case 2:
			if (!isalnum(*ch)) {
				*ch = 0;
				return container;
			}
			ch++;
			break;
		}
	}
	*ch = 0;
    
	return container;
}

  /*********************************************************************************************************/
 /****** Implémentation de la méthode chargement de l'image dans un tableau à une seule dimension *********/
/*********************************************************************************************************/

int Image::loadImage(string filename) {

	char *buffer;
    // allocation de la mémoire
	buffer = (char*) calloc(80, sizeof(char));
    
    // création d'un fichier
    FILE *file;
    // ouverture du fichier depuis le nom passait en paramètre
    file = fopen(filename.c_str(), "rb");
	
    // lecture de la première ligne (ligne du type
	getImageInformations(file, buffer);
    
    // Récupératon de la largeur de l'image
	_width = stoi(getImageInformations(file, buffer));
    
    // Récupération de la longueur de l'image
	_height = stoi(getImageInformations(file, buffer));
	
    // la couleur maximale 255 cas génral
	_max_color = stoi(getImageInformations(file, buffer));
    
    // longuer du tableau ( dans le cas général PGM)
    _size = _width * _height;
    
    // Initialsation d'un tableau
	_array = new byte[arraySize()];
    
    // Insertion des données dans le tableau
	for (int i = 0; i < arraySize(); i++) {
		fread(&_array[i], sizeof(byte), 1, file);
	}
    // fermeture du fichier
	fclose(file);
    
    // libération de la mémoire
	free(buffer);

	return 0;
}

int Image::getSize(){
	return _size;
}

int Image::get_width(){
	return _width;
}
int Image::get_height(){
	return _height;
}

  /*********************************************************************************************************/
 /****** Implémentation de la méthode stockage de l'image dans un tableau à une seule dimension ***********/
/*********************************************************************************************************/

int Image::saveImage(string filename) {
    
    // Déclaration du fichier contenant le nouveau stockage
	FILE *file;
    
	char buffer[80];

	// ouverture du fichier 
	file = fopen(filename.c_str(), "wb");
	
    /************* écriture des paramètres de l'image dans le fichier *****************/
	sprintf(buffer, "%s\n%d %d\n255\n", _type.c_str(), _width, _height);
	
    fwrite(buffer, strlen(buffer), 1, file);
     /*******************************************************************************/
    /************************** écriture du corps de l'image  **********************/
   /******************************************************************************/

	for (int i = 0; i < arraySize(); i++)
        
		fwrite(&_array[i], sizeof(byte), 1, file);

	fclose(file);

	return 0;
}

