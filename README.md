# Spring_Hibernate_TP

Spring and Hibernate TP

The documentation (TP, slides, templates) are located in the [resourcesEtudiant/](resourcesEtudiant/) folder

To start the application : `gradlew appRun`

---------------------------------------------------------

Doc partie hibernate:

-Le session factory est un singleton
-j'ai choisir de persister les entitées User et product en base
-Le mapping objet-relationnel est fait par annotations
-On crée un session pour chaque action (ici uniquement pour l'edit et le create) qui perment un point d'entré
 puis en fin d'action on kill la session. 
 Ainsi l'application n'as pas d'accés direct avec la base et ne sais connai pas dans les details les methodes qu'elle appelle.