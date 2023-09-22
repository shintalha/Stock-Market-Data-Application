create table market(
                       id bigserial constraint market_pkey primary key,
                       code varchar,
                       symbol varchar,
                       name varchar,
                       country varchar,
                       website varchar
);

create table instrument(
                           id bigserial constraint instrument_pkey primary key,
                           symbol varchar not null,
                           full_name varchar,
                           simple_name varchar,
                           market_id bigint
);

insert into market (id, code, symbol, name, country, website)
values  (1, 'XNAS', 'NASDAQ', 'NASDAQ - All Markets', 'United States of America', 'www.nasdaq.com'),
        (2, 'XNYS', 'NYSE', 'New York Stock Exchange, Inc.', 'United States of America', 'www.nyse.com'),
        (3, 'BATS', 'BATS', 'BATS Exchange', '', '');

insert into instrument (id, symbol, full_name, simple_name, market_id)
values  (1, 'AAPL', 'Apple Inc. Common Stock', 'Apple', 1),
        (2, 'BA', '', '', NULL),
        (3, 'TSLA', '', '', NULL),
        (4, 'TEAM', 'Atlassian Corporation Class A Common Stock', 'Atlassian Corporation', 1),
        (5, 'TWTR', '', '', NULL);

