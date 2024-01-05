#!/bin/bash

# ruta relativa desde el home del usuario hacia el directorio del proyecto.
D_PROJECT=/Escritorio/stcu/stcu2
# ruta completa del proyecto
D_BASE=$HOME$D_PROJECT
# ruta a la carpeta destino de los builds
D_TARGET=$HOME/shared

build_back() {
	# Ejecuta build del backend y una vez finalizado copiar archivo war al directorio shared.
	echo -e "\n---------------------------------------------------------------\n-------------- Building backend...\n---------------------------------------------------------------\n"
	cd $D_BASE/backend
	mvn clean package 
	echo -e "\n---------------------------------------------------------------\n-------------- Building backend finished\n---------------------------------------------------------------\n"
	echo -e "\n---------------------------------------------------------------\n-------------- Copy war to user directory $D_TARGET\n---------------------------------------------------------------\n"
	cp ./target/stcu2service.war $D_TARGET
	echo -e "\n---------------------------------------------------------------\n-------------- Copy war finished \n---------------------------------------------------------------\n"
	echo -e "\n---------------------------------------------------------------\n------------- Building back have completed.\n---------------------------------------------------------------\n"
}

build_front() {
	echo -e "\n---------------------------------------------------------------\n-------------- Building frontend...\n---------------------------------------------------------------\n"
	cd $D_BASE/frontend
	npm run build
	echo -e "\n---------------------------------------------------------------\n-------------- Building frontend finished\n---------------------------------------------------------------\n"
	echo -e "\n---------------------------------------------------------------\n-------------- Copying dist to user directory $D_TARGET\n---------------------------------------------------------------\n"
	cp -r dist $D_TARGET
	echo -e "\n---------------------------------------------------------------\n------------- Copy dist finished ------------------\n---------------------------------------------------------------\n" 
	echo -e "\n---------------------------------------------------------------\n------------- Building front have completed.\n---------------------------------------------------------------\n"
}

echo "comando: $0 opcion $1"

if [ $# == 0 ];
then
	echo "Falta argumento. Uso $0 b|f|a (b: build backend, f: build frontend, a: build back and front)"
	exit
fi

if [[ $1 == "b" || $1 == "a" ]];
then
	echo "build backend package"
	build_back
fi

if [[ $1 == "f" || $1 == "a" ]];
then
	echo "build frontend dist"
	build_front
fi

	

