DROP TABLE IF EXISTS requests;
CREATE TABLE requests
(
    datetime          TIMESTAMP,
    request_id        UUID,
    parent_request_id UUID,
    host              TEXT,
    type              TEXT,
    data              TEXT
);

INSERT INTO requests (datetime, request_id, parent_request_id, host, type, data)
VALUES
('2014-04-04 20:32:59.000', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', NULL, 'balancer.test.yandex.ru', 'RequestReceived', ''),
       ('2014-04-04 20:32:59.100', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', NULL, 'balancer.test.yandex.ru', 'RequestSent', 'backen'),
       ('2014-04-04 20:32:59.101', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', NULL, 'balancer.test.yandex.ru', 'RequestSent', 'backen'),
       ('2014-04-04 20:32:59.150', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa1', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', 'backend1.ru', 'RequestReceived', ''),
       ('2014-04-04 20:32:59.200', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa2', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', 'backend2.ru', 'RequestReceived', ''),
       ('2014-04-04 20:32:59.155', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa1', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', 'backend1.ru', 'RequestSent', 'backen'),
       ('2014-04-04 20:32:59.210', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa2', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', 'backend2.ru', 'ResponseSent', ''),
       ('2014-04-04 20:32:59.200', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa3', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa1', 'backend3.ru', 'RequestReceived', ''),
       ('2014-04-04 20:32:59.220', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa3', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa1', 'backend3.ru', 'ResponseSent', ''),
       ('2014-04-04 20:32:59.260', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa1', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', 'backend1.ru', 'ResponseReceived', 'backen'),
       ('2014-04-04 20:32:59.300', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa1', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', 'backend1.ru', 'ResponseSent', ''),
       ('2014-04-04 20:32:59.310', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', NULL, 'balancer.test.yandex.ru', 'ResponseReceived', 'backen'),
       ('2014-04-04 20:32:59.250', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', NULL, 'balancer.test.yandex.ru', 'ResponseReceived', 'backen'),
       ('2014-04-04 20:32:59.400', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa0', NULL, 'balancer.test.yandex.ru', 'ResponseSent', ''),
       ('2014-04-04 20:32:59.500', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa4', NULL, 'balancer.test.yandex.ru', 'RequestReceived', ''),
       ('2014-04-04 20:32:59.505', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa4', NULL, 'balancer.test.yandex.ru', 'RequestSent', 'backen'),
       ('2014-04-04 20:32:59.510', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa5', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa4', 'backend1.ru', 'RequestReceived', ''),
       ('2014-04-04 20:32:59.700', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa5', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa4', 'backend1.ru', 'ResponseSent', ''),
       ('2014-04-04 20:32:59.710', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa4', NULL, 'balancer.test.yandex.ru', 'ResponseReceived', 'backen'),
       ('2014-04-04 20:32:59.715', 'aaeebcaa-acab-aea8-bbad-abbabdaaaaa4', NULL, 'balancer.test.yandex.ru', 'ResponseSent', '');