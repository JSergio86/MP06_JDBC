CREATE TABLE Jugadores (
idJugador INT PRIMARY KEY,
rank TEXT,
wins INT,
kills INT,
deaths INT,
assists INT,
scoreround FLOAT,
kad FLOAT,
killsround FLOAT,
plants INT,
firstbloods INT,
clutches INT,
flawless INT,
aces INT
);

CREATE TABLE Mapas (
idMapa INT PRIMARY KEY,
name TEXT,
porcentaje_win TEXT,
wins INT,
losses INT,
kd FLOAT,
adr FLOAT,
acs FLOAT
);

CREATE TABLE Partidas (
idJugador INT,
idMapa INT,
idPartida INT PRIMARY KEY,
type TEXT,
result TEXT,
FOREIGN KEY (idJugador) REFERENCES Jugadores(idJugador),
FOREIGN KEY (idMapa) REFERENCES Mapas(idMapa)
);


CREATE TABLE Agentes(
idAgente INT PRIMARY KEY,
name TEXT,
type TEXT
);

CREATE TABLE JugadorAgentes (
idJugador INT,
idAgente INT,
timePlayed TEXT,
matches INT,
win TEXT,
kd FLOAT,
adr FLOAT,
acs FLOAT,
hs TEXT,
kast TEXT,
FOREIGN KEY (idJugador) REFERENCES Jugadores(idJugador),
FOREIGN KEY (idAgente) REFERENCES Agentes(idAgente)
);

CREATE TABLE Armas (
idArma INT PRIMARY KEY,
name TEXT,
type TEXT
);

CREATE TABLE JugadorArmas (
idJugador INT,
idArma INT,
kills INT,
deaths INT,
headshots TEXT,
damageRound FLOAT,
killsRound FLOAT,
longestKill TEXT,
FOREIGN KEY (idJugador) REFERENCES Jugadores(idJugador),
FOREIGN KEY (idArma) REFERENCES Armas(idArma)
);