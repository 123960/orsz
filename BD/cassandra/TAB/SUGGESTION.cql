CREATE TABLE orsz.SUGGESTION (
    name      TEXT,
    id        TEXT,
    version   TEXT,
    content   TEXT,
    upvotes   INT,
    downvotes INT,
  PRIMARY KEY ((id), name)
)
