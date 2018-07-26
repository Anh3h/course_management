SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `course_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `course`
--

CREATE TABLE `course` (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) NOT NULL,
  `credit_value` varchar(255) NOT NULL,
  `outline` varchar(255) DEFAULT NULL,
  `semester` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `course`
--

INSERT INTO `course` (`id`, `code`, `credit_value`, `outline`, `semester`, `title`) VALUES
(1, 'CEF201', '4', 'Lesson plan.pdf', 'First Semester', 'Analysis'),
(7, 'CEF 205', '4', 'Lesson plan.pdf', 'First Semester', 'Boolean Algebra'),
(9, 'CEF 308', '4', 'Lesson plan.pdf', 'First Semester', 'C/C++ Programming'),
(11, 'CEF406', '4', 'Lesson_plan.pdf', 'Second Semester', 'Object Oriented programming'),
(12, 'CEF 512', '4', 'k8-template.yml', 'Second Semester', 'ERP'),
(13, 'CEF 210', '4', 'Lesson_plan.pdf', 'First Semester', 'Hunger and starvation management');

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE `department` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`id`, `name`) VALUES
(1, 'Computer Engineering'),
(2, 'Electrical Engineering'),
(3, 'Mechanical Engineering'),
(5, 'Civil Engineering'),
(10, 'Petro-chemical engineering');

-- --------------------------------------------------------

--
-- Table structure for table `department_course`
--

CREATE TABLE `department_course` (
  `course_id` bigint(20) NOT NULL,
  `department_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `department_course`
--

INSERT INTO `department_course` (`course_id`, `department_id`) VALUES
(9, 1),
(1, 2),
(1, 1),
(1, 3),
(7, 1),
(1, 10),
(11, 1),
(12, 5);

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(14),
(14),
(14),
(14);

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `name`) VALUES
(1, 'ADMIN'),
(2, 'LECTURER'),
(3, 'STUDENT');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `email`, `first_name`, `last_name`, `password`, `telephone`, `username`, `role_id`) VALUES
(1, 'admin@mail.com', 'Admin', 'Admin', '$2a$10$OSVh6T1Ah20r7NT9bAU5Xug8gEWYMegyHBu2XBARfE1xKPWRc0qNW', '', 'admin', 1),
(2, 'janedoe@mail.com', 'Jane', 'Doe', '$2a$10$OSVh6T1Ah20r7NT9bAU5Xug8gEWYMegyHBu2XBARfE1xKPWRc0qNW', '675894586', 'jane', 2),
(3, 'johndoe@mail.com', 'John', 'Doe.', '$2a$10$Ec0C9QDBNspwo7GC4D0z7eoHCyv8EXMCdTJcM6uuIlxE83fsEwZ6u', '', 'john', 2),
(4, 'mike-mill@mail.cm', 'Mike', 'Mill', '$2a$10$1IIkSp7.f6vkdsmtEw2p7OadCoxPt2/W7wGuYEREYqX8o7OfQBR1e', '679851246', 'mike', 3),
(8, 'courageangeh@gmail.com', 'Courage', 'Angeh', '$2a$10$OTr/Dvhv3u8NQnEI7ziN0ezBA9SzdyzN5NGKBlpYsWer5l2Pnj86a', '+237671931529', 'courage', 3);

-- --------------------------------------------------------

--
-- Table structure for table `user_course`
--

CREATE TABLE `user_course` (
  `course_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_course`
--

INSERT INTO `user_course` (`course_id`, `user_id`) VALUES
(1, 2),
(1, 8),
(9, 8),
(7, 8),
(9, 2),
(7, 2),
(12, 8);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `course`
--
ALTER TABLE `course`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_i60mruj0y7a7vs99dqpiye7en` (`code`),
  ADD UNIQUE KEY `UK_msgoex7rold2eqqf1cllhk02i` (`title`);

--
-- Indexes for table `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_1t68827l97cwyxo9r1u6t4p7d` (`name`);

--
-- Indexes for table `department_course`
--
ALTER TABLE `department_course`
  ADD KEY `FK7iqy10owpdwhv1r2qe50i3ejd` (`course_id`),
  ADD KEY `FK5lv22lb7yc6ew05u2c6ppkhe2` (`department_id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_8sewwnpamngi6b1dwaa88askk` (`name`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  ADD UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`),
  ADD KEY `FKn82ha3ccdebhokx3a8fgdqeyy` (`role_id`);

--
-- Indexes for table `user_course`
--
ALTER TABLE `user_course`
  ADD KEY `FKka18m18kpimodvy8yg2icu083` (`course_id`),
  ADD KEY `FKpv8tt3252hb6kyej8p7e7pokl` (`user_id`);
