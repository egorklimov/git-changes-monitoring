create view commit_log
(
    repository_id,   -- is used for filtering
    branch_name,     -- is used for filtering
    commit_hash,
    commit_author,
    commit_date,
    commit_message,
    commit_day_of_year,
    commit_day_of_month,
    commit_day_of_week,
    commit_year,
    commit_month,
    commit_hour
) as
select repository.id                          as repository_id,
       branch.name                            as branch_name,
       commit.hash                            as commit_hash,
       commit.author                          as commit_author,
       commit.commit_date                     as commit_date,
       commit.message                         as commit_message,
       extract(doy   from commit.commit_date) as commit_day_of_year,
       extract(day   from commit.commit_date) as commit_day_of_month,
       extract(dow   from commit.commit_date) as commit_day_of_week,
       extract(year  from commit.commit_date) as commit_year,
       extract(month from commit.commit_date) as commit_month,
       extract(hour  from commit.commit_date) as commit_hour
from repository
    join branch
        on branch.repository_id = repository.id
    join branch_contains_commit
        on branch_contains_commit.branch_id = branch.id
    join commit
        on commit.hash = branch_contains_commit.commit_hash
order by commit_date;
