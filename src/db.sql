CREATE Table report(
                       reportId int not null AUTO_INCREMENT primary KEY ,
                       postId int,
                       commentId int,
                       reason VARCHAR(255),
                       content VARCHAR(255),
                       time timestamp,
                       FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE cascade,
                       FOREIGN KEY (commentId) REFERENCES comment(commentId) ON DELETE cascade
);
CREATE Table post(
                     postId int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                     nameAuthor VARCHAR(50),
                     userId int,
                     title varchar(1024),
                     tags varchar(100),
                     type varchar(10),
                     content TEXT,
                     time timestamp,
                     FOREIGN KEY (userId) REFERENCES user(userId) ON DELETE cascade
);
CREATE Table interaction(
                            interactionId int AUTO_INCREMENT primary key,
                            userId int,
                            postId int,
                            commentId int,
                            Type varchar(255),
                            time timestamp,
                            FOREIGN KEY (userId) REFERENCES user(userId) ON DELETE cascade,
                            FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE cascade
);
Create Table comment (
                         commentId int AUTO_INCREMENT primary key,
                         parentComment int,
                         postId int,
                         username VARCHAR(50),
                         userId int,
                         content text,
                         time timestamp,
                         FOREIGN KEY (userId) REFERENCES user(userId) ON DELETE cascade,
                         FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE cascade
);
CREATE TABLE user(
                     userId int AUTO_INCREMENT primary key,
                     userName varchar(100) unique,
                     phone VARCHAR(10),
                     email varchar(100),
                     password varchar(100),
                     gender VARCHAR(10),
                     avatar varchar(255),
                     role varchar(50),
                     hidden BOOLEAN DEFAULT FALSE
);
CREATE Table follow(
                       followId int AUTO_INCREMENT primary key,
                       userIdSrc int, -- người đi follow
                       userIdDst int, -- người được follow
                       time timestamp,
                       FOREIGN KEY (userIdSrc) REFERENCES user(userId) ON DELETE cascade,
                       FOREIGN KEY (userIdDst) REFERENCES user(userId) ON DELETE cascade
);
CREATE TABLE notifications (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               message VARCHAR(1024) NOT NULL,
                               postId INT NOT NULL,
                               userId INT NOT NULL,
                               state INT DEFAULT 1, -- 1 chưa xem, 0 đã xem
                               time TIMESTAMP,
                               FOREIGN KEY (userId) REFERENCES user(userId) ON DELETE cascade,
                               FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE cascade
);
insert into user(userId,userName,email,password,gender,avatar,role)
values(1,'admin','user1@gmail.com','12345','Male','1.png','user');
insert into user(userId,userName,email,password,gender,avatar,role)
values(2,'user1','user1@gmail.com','12345','Male','2.png','user');
insert into user(userId,userName,email,password,gender,avatar,role)
values(3,'user2','user2@gmail.com','12345','Male','3.png','user');
insert into user(userId,userName,email,password,gender,avatar,role)
values(4,'user3','user3@gmail.com','12345','Female','4.png','user');
insert into user(userId,userName,email,password,gender,avatar,role)
values(5,'user4','user4@gmail.com','12345','Female','5.png','user');
insert into user(userId,userName,email,password,gender,avatar,role)
values(6,'user5','user5@gmail.com','12345','Male','6.png','user');


