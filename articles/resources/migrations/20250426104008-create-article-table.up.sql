create table if not exists article (id uuid default random_uuid() primary key,
                                        title varchar,
                                        author varchar,
                                        date date,
                                        doi varchar);