

#include<string>


using namespace std;
typedef unsigned char byte;

  /*******************************************************************************************************/
 /************* classe  image contenant les attributs et les methodes communes entre PGP et PPM *********/
/*******************************************************************************************************/

class Image {

protected:
	string _type;// type de l'image P1----P6 Binaire
	int _width;// largeur de l'image
	int _height;// longueur
    int _max_color; // niveau maximal de la couleur
	int _size; // taille du tableau
	byte *_array; //tableau à une seule dimension contenant le corps de l'image

public:
	
    /*****************  Constructeur par défaut ***************/
	Image() {};
	
    /*********** Constructeur avec des paramètres largeur et longueur ***************/
	Image(int x, int y);
	
     /***************************  Chargement d'une image ********************************/
    /*************** Méthode commune etre les deux classes ImagePPM et ImagePGM *********/	

	int loadImage(string filename) ;
	 /************************************* Sauvegarder une image *************************/
	/*************** Méthode commune etre les deux classes ImagePPM et ImagePGM **********/
   /*************************************************************************************/	
	int saveImage(string filename);

    /************ Calculer la taille du tableau ***********************/
    virtual int arraySize()=0;
    
	/************ Retrouver le pixel d'une image dans un tableau à partir des deux coordonnées (méthode virtual car le format de l'image PPM et PGM est différent *****/
	virtual int findPixel(int x, int y)=0;

	
	int getSize();
	int get_width();
	int get_height();


};


