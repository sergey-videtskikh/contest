WITH RECURSIVE tmp AS (
    SELECT "request_id", "parent_request_id", 1 AS "depth"/*, datetime, host, type*/
    FROM requests
    WHERE "parent_request_id" IS NULL
    UNION
    /*ALL*/
    SELECT e."request_id", e."parent_request_id", t."depth" + 1/*, e.datetime, e.host, e.type*/
    FROM tmp t
             INNER JOIN requests e ON e."parent_request_id" = t."request_id"
)
SELECT "parent_request_id", "request_id", "depth"/*, datetime, host, type*/
FROM tmp