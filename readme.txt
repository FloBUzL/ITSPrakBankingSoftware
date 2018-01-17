Vorgenommene �nderungen an der Implementierung:
Zu gro�en Teilen Refactoring um bestimmte Sicherheitsfeatures besser implementieren zu k�nnen. 
Die Package- sowie Klassennamen sollten jedoch selbsterkl�rend sein.
Eine wichtige �nderung betrifft jedoch die angelegten Dateien im Falle eines registrierten Devices:
Die Datei enth�lt nun Device-Code;Auth-Code. Der Auth-Code l�sst sich jedoch deterministisch bestimmen,
somit bleibt der Auth-Code auch bei einem Neustart des Servers gleich. (Sofern das Device nat�rlich in 
der Datenbank eingetragen wurde. Dies muss weiterhin manuell geschehen.)
Zudem wird der Zugang nun nach einer Anzahl von fehlerhaften Authentifizierungen (Login + Device) 
f�r eine Weile geblockt. Beides kann in shared.constants.Misc beeinflusst werden. (Serverseitig!)
Die User-ID spielt nun keine Rolle mehr, da diese sich mit einer �nderung der Datenbank schnell
�ndern kann. Daf�r ist nun der Benutzername wichtig.

Debugging-Ausgaben werden nun �ber shared.constants.Misc.DEBUG gesteuert. Ist die Konstante auf
false gesetzt, werden mit ConnectionData.debug() keine Ausgaben mehr get�tigt. (ConnectionData.log()
t�tigt jedoch weiterhin Ausgaben.)

Vorgenommene �nderungen am Protokoll:
Manche Daten werden nun AES-verschl�sselt �bertragen. Meistens handelt es sich dabei um 
Challenge-Response-Daten. Jedoch werden auch die Balance-Daten sowie der Transaktionsempf�nger
verschl�sselt.

1. Login-Prozess:
1.1 Diffie-Hellman-Schl�sselaustausch zwischen Server und Client
1.2 Client erh�lt vom Server eine Nonce.
1.3 Client authentifiziert sich via Challenge-Response

2. Device-Registrierung sowie Authentifizierung:
2.1 Der Auth-Code ist nun kein trivialer Bestandteil des Device-Codes, l�sst sich jedoch weiterhin
deterministisch berechnen
2.2 Die Authentifizierung geschieht auch hier via Challenge-Response - hash(auth-code + nonce) =: cr

Die run*.sh-Files sind nicht zwangsweise auf jedem System lauff�hig