Vorgenommene Änderungen an der Implementierung:
Zu großen Teilen Refactoring um bestimmte Sicherheitsfeatures besser implementieren zu können. 
Die Package- sowie Klassennamen sollten jedoch selbsterklärend sein.
Eine wichtige Änderung betrifft jedoch die angelegten Dateien im Falle eines registrierten Devices:
Die Datei enthält nun Device-Code;Auth-Code. Der Auth-Code lässt sich jedoch deterministisch bestimmen,
somit bleibt der Auth-Code auch bei einem Neustart des Servers gleich. (Sofern das Device natürlich in 
der Datenbank eingetragen wurde. Dies muss weiterhin manuell geschehen.)
Zudem wird der Zugang nun nach einer Anzahl von fehlerhaften Authentifizierungen (Login + Device) 
für eine Weile geblockt. Beides kann in shared.constants.Misc beeinflusst werden. (Serverseitig!)
Die User-ID spielt nun keine Rolle mehr, da diese sich mit einer Änderung der Datenbank schnell
ändern kann. Dafür ist nun der Benutzername wichtig.

Debugging-Ausgaben werden nun über shared.constants.Misc.DEBUG gesteuert. Ist die Konstante auf
false gesetzt, werden mit ConnectionData.debug() keine Ausgaben mehr getätigt. (ConnectionData.log()
tätigt jedoch weiterhin Ausgaben.)

Vorgenommene Änderungen am Protokoll:
Manche Daten werden nun AES-verschlüsselt übertragen. Meistens handelt es sich dabei um 
Challenge-Response-Daten. Jedoch werden auch die Balance-Daten sowie der Transaktionsempfänger
verschlüsselt.

1. Login-Prozess:
1.1 Diffie-Hellman-Schlüsselaustausch zwischen Server und Client
1.2 Client erhält vom Server eine Nonce.
1.3 Client authentifiziert sich via Challenge-Response

2. Device-Registrierung sowie Authentifizierung:
2.1 Der Auth-Code ist nun kein trivialer Bestandteil des Device-Codes, lässt sich jedoch weiterhin
deterministisch berechnen
2.2 Die Authentifizierung geschieht auch hier via Challenge-Response - hash(auth-code + nonce) =: cr

Die run*.sh-Files sind nicht zwangsweise auf jedem System lauffähig