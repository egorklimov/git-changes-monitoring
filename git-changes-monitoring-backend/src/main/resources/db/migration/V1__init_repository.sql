-- tag aggregates repositories, it may be a group of repositories owned by organization/user
-- or some other group created by user
create table tag
(
    id              BIGSERIAL    PRIMARY KEY,
    name            VARCHAR(150) NOT NULL UNIQUE,
    description     VARCHAR(200) NOT NULL DEFAULT ''
);

create table repository
(
    id          BIGSERIAL    PRIMARY KEY,
    path        VARCHAR(500) UNIQUE NOT NULL,
    name        VARCHAR(500) NOT NULL,
    is_syncing  BOOLEAN      NOT NULL
);

create table repository_has_tag
(
    tag_id        BIGINT REFERENCES tag(id)         ON DELETE CASCADE NOT NULL,
    repository_id BIGINT REFERENCES repository(id)  ON DELETE CASCADE NOT NULL
);

create table branch
(
    id              BIGSERIAL      PRIMARY KEY,
    repository_id   BIGINT         REFERENCES repository(id)    ON DELETE CASCADE   NOT NULL,
    name            VARCHAR(250)   NOT NULL,
    is_scanned      BOOLEAN        NOT NULL,
    UNIQUE(name, repository_id)
);

create table contributor
(
    id      BIGSERIAL PRIMARY KEY,
    mail    VARCHAR(500) NOT NULL UNIQUE,
    name    VARCHAR(500) NOT NULL
);

create table commit
(
    id                  BIGSERIAL    PRIMARY KEY,
    hash                VARCHAR(40)  NOT NULL,
    committer           VARCHAR(500) REFERENCES contributor(mail) NOT NULL,
    author              VARCHAR(500) REFERENCES contributor(mail) NOT NULL,
    branch_id           BIGINT       REFERENCES branch(id) ON DELETE CASCADE NOT NULL,
    commit_date         TIMESTAMP    NOT NULL,
    author_date         TIMESTAMP    NOT NULL,
    message             TEXT         NOT NULL,
    UNIQUE(hash, branch_id)
);
