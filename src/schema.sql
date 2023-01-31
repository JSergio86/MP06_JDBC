CREATE TABLE Jugadores (
idJugador TEXT PRIMARY KEY,
rank TEXT,
wins TEXT,
kills TEXT,
deaths TEXT,
assists TEXT,
scoreround TEXT,
kad TEXT,
killsround TEXT,
plants TEXT,
firstbloods TEXT,
clutches TEXT,
flawless TEXT,
aces TEXT
);

CREATE TABLE Mapas (
idMapa TEXT PRIMARY KEY,
name TEXT,
porcentaje_win TEXT,
wins TEXT,
losses TEXT,
kd TEXT,
adr TEXT,
acs TEXT
);

CREATE TABLE Partidas (
idJugador TEXT,
idMapa TEXT ,
idPartida TEXT PRIMARY KEY,
type TEXT,
result TEXT
);


CREATE TABLE Agentes(
idAgente TEXT PRIMARY KEY,
name TEXT,
type TEXT
);

CREATE TABLE PlayerAgentes (
idJugador TEXT,
idAgente TEXT,
timePlayed TEXT,
matches TEXT,
win TEXT,
kd TEXT,
adr TEXT,
acs TEXT,
hs TEXT,
kast TEXT
);

CREATE TABLE Armas (
idArma TEXT PRIMARY KEY,
name TEXT,
type TEXT
);

CREATE TABLE PlayerWeapons (
idJugador TEXT,
idArma TEXT,
kills TEXT,
deaths TEXT,
headshots TEXT,
damageRound TEXT,
killsRound TEXT,
longestKill TEXT
);

