OBSAH TOHOTO SOUBORU
---------------------
   
 * Úvod
 * Požadavky
 * Instalace
 * Nastavení

ÚVOD
------------

Modul My ParsimoniousPathOpenings hledá dlouhé nepravidelné struktury v obraze.

POŽADAVKY
------------

Pro zprovoznění modulu je potřeba:
	ImageJ 1.48v nebo novější(modul byl vytvořen pro něj, pravděpodobně bude fungovat i pro některé starší verze)

INSTALACE
------------
 
Stačí zkopírovat složku PV162 do adresáře ...\ImageJ\plugins.
Pro spuštění se poté stačí kliknout na >Plugins>PV162>My ParsimoniousPathOpenings.

NASTAVENÍ
-------------

1. Otevřete zdrojový soubor ...\ImageJ\plugins\PV162\My_ParsimoniousPathOpenings.java.
2. Následujte instrukce napsané v komentářích ve funkci run(třída My_ParsimoniousPathOpenings).

Možnosti(řádky 1521 až 1530):
	int openingSize		- minimální délka struktur, které hledáme
	int beta		- parametr pro případ hledání struktur pomocí metod getPathOpeningsBMP, addPathsBMP, showPathsBMP
	boolean SN		- true pro povolení hledání struktur ve směru zdola nahoru, false jinak
	boolean SWNE		- true pro povolení hledání struktur ve směru ze spodního levého rohu do horního pravého rohu, false jinak
	boolean WE		- true pro povolení hledání struktur ve směru zleva doprava, false jinak
	boolean NWSE		- true pro povolení hledání struktur ve směru z horního levého rohu do spodního pravého rohu, false jinak
	boolean inversion	- true pro hledání struktur tmavších než zbytek obrázku(pro inverzi obrazu před a po zpracování), false jinak
	boolean blackBG		- true pro vynulování intenzit pixelů pozadí(pixelů s intenzitou < cutOff), false jinak
				- zajišťuje, aby se v šedotónových obrazech objevily pouze hledané struktury a ne celé cesty 
	int cutOff		- při blackBG == true se intenzita pixelů s menší intenzitou změní na 0

Pro správné fungování by měl být odkomentován právě jeden z následujících řádků(řádky 1548 až 1556):

	//lip = getPathOpeningsGMP(ip, openingSize, SN, SWNE, WE, NWSE); //hledání struktur pomocí globálně maximálních cest v obrázku
	//lip = getPathOpeningsLMP(ip, openingSize, SN, SWNE, WE, NWSE); //hledání struktur pomocí lokálně maximálních cest v obrázku
	//lip = getPathOpeningsBMP(ip, openingSize, beta, SN, WE);	 //hledání struktur pomocí beta-maximálních cest v obrázku
	//lip = addPathsGMP(ip, SN, SWNE, WE, NWSE);			 //přidání globálně maximálních cest do obrázku
	//lip = addPathsLMP(ip, SN, SWNE, WE, NWSE);			 //přidání lokálně maximálních cest do obrázku
	//lip = showPathsGMP(ip, SN, SWNE, WE, NWSE);			 //hledání globálně maximálních cest v obrázku
	//lip = showPathsLMP(ip, SN, SWNE, WE, NWSE);			 //hledání lokálně maximálních cest v obrázku
	//lip = addPathsBMP(ip, beta, SN, WE);				 //přidání beta-maximálních cest do obrázku
	//lip = showPathsBMP(ip, beta, SN, WE);				 //hledání beta-maximálních cest v obrázku


CONTENTS OF THIS FILE
---------------------
   
 * Introduction
 * Requirements
 * Installation
 * Configuration

INTRODUCTION
------------

My ParsimoniousPathOpenings module searches for long uneven structures.

REQUIREMENTS
------------

This module requires:
	ImageJ 1.48v or later

INSTALLATION
------------
 
Copy PV162 folder to ...\ImageJ\plugins.
Click >Plugins>PV162>My ParsimoniousPathOpenings to run.

CONFIGURATION
-------------

1. Open the source file ...\ImageJ\plugins\PV162\My_ParsimoniousPathOpenings.java.
2. Follow the instructions written as comments in function run(class My_ParsimoniousPathOpenings).
