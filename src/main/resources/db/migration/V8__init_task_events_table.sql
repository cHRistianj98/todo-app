DROP TABLE IF EXISTS task_events;
CREATE TABLE task_events
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    task_id    INT,
    occurrence DATETIME,
    name       VARCHAR(30)
);
