drop schema test cascade;

create schema test;

create table if not exists test.users
(
    id           varchar(16) not null
        primary key,
    creation_date timestamp,
    last_edit_date timestamp,
    name         varchar(20),
    parent_name   varchar(20),
    password     varchar(80),
    surname      varchar(20),
    username     varchar(40)
);

create table if not exists test.news
(
    id            bigserial
        primary key,
    creation_date  timestamp,
    last_edit_date  timestamp,
    text          varchar(2000),
    title         varchar(150),
    inserted_by_id varchar(16)
            references test.users,
    updated_by_id  varchar(16)
            references test.users
);

alter table test.news
    owner to postgres;

create table if not exists test.comments
(
    id            bigserial
        primary key,
    creation_date  timestamp,
    text          varchar(300),
    news_id       bigint
            references test.news,
    inserted_by_id varchar(16)
            references test.users
);

alter table test.comments
    owner to postgres;

insert into test.users (id, creation_date, last_edit_date, name, parent_name, password, surname, username)
VALUES ('1', current_timestamp, null, 'yuru', 'vladimirovich', 'password', 'garanovich', 'vanderon');

insert into test.news(id, creation_date, last_edit_date, title, text)
VALUES (1, current_timestamp, null, 'Breakthrough in Renewable Energy',
        'Scientists have developed an innovative solar panel technology that increases efficiency by 30%.'),
       (2, current_timestamp, null, 'Global Economy Shows Signs of Recovery',
        'The latest reports indicate a steady improvement in the global economy, led by a significant uptick in consumer spending.'),
       (3, current_timestamp, null, 'Health Experts Warn of Upcoming Flu Season',
        'With winter approaching, health officials emphasize the importance of vaccinations to combat the aggressive flu strain expected this year.'),
       (4, current_timestamp, null, 'New Study Reveals Impact of Climate Change',
        'A recent study highlights alarming changes in wildlife migration patterns due to climate change, urging immediate action.'),
       (5, current_timestamp, null, 'Tech Giants Invest in AI Research',
        'Major tech companies are ramping up investments in artificial intelligence, aiming to lead the future of technology.'),
       (6, current_timestamp, null, 'Sports Team Wins Championship',
        'In a thrilling final, the local sports team clinched the championship title, bringing pride to the community.'),
       (7, current_timestamp, null, 'New Education Policies Announced',
        'The government has announced sweeping education reforms aimed at improving access and quality in schools.'),
       (8, current_timestamp, null, 'Breakthrough in Cancer Research',
        'Scientists have identified new biomarkers that could lead to more effective cancer treatments and early detection methods.'),
       (9, current_timestamp, null, 'City Launches Green Initiative',
        'The city council has launched a comprehensive green initiative aimed at promoting sustainability and reducing waste.'),
       (10, current_timestamp, null, 'Blockchain Technology Gains Traction',
        'Blockchain continues to disrupt traditional industries, with experts predicting widespread adoption in the coming years.'),
       (11, current_timestamp, null, 'Wildfires Devastate State Park',
        'A series of wildfires have caused extensive damage to one of the state’s beloved parks, leading to evacuations and closures.'),
       (12, current_timestamp, null, 'New Artisan Market Opens Downtown',
        'A vibrant new artisan market has opened in the downtown area, showcasing local crafts and goods.'),
       (13, current_timestamp, null, 'Space Agency Plans Mars Mission',
        'In an ambitious plan, the space agency has announced a mission to send humans to Mars within the next decade.'),
       (14, current_timestamp, null, 'Local Restaurant Receives Michelin Star',
        'A local restaurant has earned its first Michelin star, recognizing its outstanding culinary achievements.'),
       (15, current_timestamp, null, 'International Film Festival Kicks Off',
        'The annual International Film Festival has begun, featuring films from around the globe and highlighting emerging filmmakers.'),
       (16, current_timestamp, null, 'New Wildlife Conservation Initiative Launched',
        'Conservationists have launched a new initiative aimed at protecting endangered species and their habitats.'),
       (17, current_timestamp, null, 'Tech Conference Reveals Future Innovations',
        'This year’s tech conference showcased groundbreaking innovations that promise to enhance our daily lives.'),
       (18, current_timestamp, null, 'Historic Peace Agreement Signed',
        'Leaders from two long-adversarial countries have signed a historic peace agreement, signaling hopes for stability in the region.'),
       (19, current_timestamp, null, 'Cryptocurrency Market Volatility Continues',
        'The cryptocurrency market remains highly volatile, with experts advising investors to tread cautiously amid fluctuating values.'),
       (20, current_timestamp, null, 'City Implements New Traffic Regulations',
        'In an effort to reduce congestion, the city has introduced new traffic regulations targeted at improving urban mobility.');

-- Comments for News ID 1
INSERT INTO test.comments (id, creation_date, text, news_id)
VALUES (1, current_timestamp, 'Great article on renewable energy!', 1),
       (2, current_timestamp, 'I hope to see more construction of solar farms.', 1),
       (3, current_timestamp, 'The future is bright for green energy!', 1),
       (4, current_timestamp, 'Innovations in clean tech are essential.', 1),
       (5, current_timestamp, 'Let’s invest in sustainable resources!', 1),
       (6, current_timestamp, 'How will this affect energy prices?', 1),
       (7, current_timestamp, 'Looking forward to policy changes!', 1),
       (8, current_timestamp, 'We need to educate the public on these topics.', 1),
       (9, current_timestamp, 'Great to see increasing government support.', 1),
       (10, current_timestamp, 'This is a step in the right direction.', 1),

-- Comments for News ID 2
       (11, current_timestamp, 'The economy impacts us all!', 2),
       (12, current_timestamp, 'What measures are being taken to stabilize it?', 2),
       (13, current_timestamp, 'Inflation is a concern for many households.', 2),
       (14, current_timestamp, 'Great insights on job growth projections.', 2),
       (15, current_timestamp, 'This affects small businesses significantly.', 2),
       (16, current_timestamp, 'Let’s discuss long-term solutions.', 2),
       (17, current_timestamp, 'What industries are booming right now?', 2),
       (18, current_timestamp, 'We need better financial education.', 2),
       (19, current_timestamp, 'Are the policies effective?', 2),
       (20, current_timestamp, 'Fingers crossed for a recovery!', 2),

-- Comments for News ID 3
       (21, current_timestamp, 'I always get my flu shot!', 3),
       (22, current_timestamp, 'What are the side effects this year?', 3),
       (23, current_timestamp, 'Flu season can be rough.', 3),
       (24, current_timestamp, 'Please keep awareness high.', 3),
       (25, current_timestamp, 'What precautions should we take?', 3),
       (26, current_timestamp, 'Vaccination is key for prevention.', 3),
       (27, current_timestamp, 'Hope everyone stays healthy this season!', 3),
       (28, current_timestamp, 'Let’s be proactive about flu prevention.', 3),
       (29, current_timestamp, 'Eager to see the statistics this year.', 3),
       (30, current_timestamp, 'We should also focus on educating others.', 3),

-- Comments for News ID 4
       (31, current_timestamp, 'Climate change is such a pressing issue!', 4),
       (32, current_timestamp, 'We must take drastic action to support the environment.', 4),
       (33, current_timestamp, 'It’s great to see tech companies involved!', 4),
       (34, current_timestamp, 'We need more global initiatives!', 4),
       (35, current_timestamp, 'Excited about these new projects!', 4),
       (36, current_timestamp, 'Hope other sectors join the cause.', 4),
       (37, current_timestamp, 'Every little bit helps in the fight.', 4),
       (38, current_timestamp, 'Climate awareness is vital!', 4),
       (39, current_timestamp, 'What about ocean cleanup efforts?', 4),
       (40, current_timestamp, 'This kind of change is so necessary.', 4),

-- Comments for News ID 5
       (41, current_timestamp, 'AI has so much potential!', 5),
       (42, current_timestamp, 'We must also consider ethical implications.', 5),
       (43, current_timestamp, 'This could change industries dramatically.', 5),
       (44, current_timestamp, 'Looking forward to seeing advancements!', 5),
       (45, current_timestamp, 'Will AI improve efficiency?', 5),
       (46, current_timestamp, 'We need guidelines to ensure fairness.', 5),
       (47, current_timestamp, 'Can’t wait to see new applications!', 5),
       (48, current_timestamp, 'Innovation should be inclusive.', 5),
       (49, current_timestamp, 'Keep us updated on developments!', 5),
       (50, current_timestamp, 'This technology is fascinating!', 5),

-- Comments for News ID 6
       (51, current_timestamp, 'More pet adoption stories please!', 6),
       (52, current_timestamp, 'What a heartwarming story!', 6),
       (53, current_timestamp, 'Adopting pets saves lives.', 6),
       (54, current_timestamp, 'Such a sweet dog!', 6),
       (55, current_timestamp, 'We need more awareness on rescues.', 6),
       (56, current_timestamp, 'How can I help local shelters?', 6),
       (57, current_timestamp, 'Those who adopt are heroes.', 6),
       (58, current_timestamp, 'Hope to see more happy endings!', 6),
       (59, current_timestamp, 'Adoption is an act of love.', 6),
       (60, current_timestamp, 'Fantastic initiative!', 6),

-- Comments for News ID 7
       (61, current_timestamp, 'Excited about the upcoming concert!', 7),
       (62, current_timestamp, 'What an amazing lineup of artists!', 7),
       (63, current_timestamp, 'I cannot wait to hear my favorite bands live!', 7),
       (64, current_timestamp, 'Will there be food trucks at this event?', 7),
       (65, current_timestamp, 'Such a good opportunity for live music!', 7),
       (66, current_timestamp, 'Let’s not forget to support local artists!', 7),
       (67, current_timestamp, 'Hope the weather holds up!', 7),
       (68, current_timestamp, 'Great venue choice for the concert!', 7),
       (69, current_timestamp, 'Any tips for the best way to get there?', 7),
       (70, current_timestamp, 'Looking forward to an unforgettable night!', 7),

-- Comments for News ID 8
       (71, current_timestamp, 'Big changes in the tech world ahead!', 8),
       (72, current_timestamp, 'What will this mean for data privacy?', 8),
       (73, current_timestamp, 'Hope it leads to more user-friendly apps.', 8),
       (74, current_timestamp, 'Is there a focus on cybersecurity?', 8),
       (75, current_timestamp, 'Exciting innovations coming our way!', 8),
       (76, current_timestamp, 'Tech should be accessible for all.', 8),
       (77, current_timestamp, 'What are the key takeaways from this report?', 8),
       (78, current_timestamp, 'Looking forward to the developments!', 8),
       (79, current_timestamp, 'How will this impact small businesses?', 8),
       (80, current_timestamp, 'Let’s keep up with the trends!', 8),

-- Comments for News ID 9
       (81, current_timestamp, 'Mental health awareness is crucial!', 9),
       (82, current_timestamp, 'So necessary to highlight these issues.', 9),
       (83, current_timestamp, 'We need more resources for support!', 9),
       (84, current_timestamp, 'Can we organize community events?', 9),
       (85, current_timestamp, 'It’s okay to not be okay.', 9),
       (86, current_timestamp, 'This topic needs to be more normalized.', 9),
       (87, current_timestamp, 'How can we support mental health initiatives?', 9),
       (88, current_timestamp, 'Great insights on mental wellness!', 9),
       (89, current_timestamp, 'Let’s break the stigma around mental health.', 9),
       (90, current_timestamp, 'Keep advocating for mental health.', 9),

-- Comments for News ID 10
       (91, current_timestamp, 'Exciting advancements in space exploration!', 10),
       (92, current_timestamp, 'What will be discovered next?', 10),
       (93, current_timestamp, 'Space knowledge is essential for our future.', 10),
       (94, current_timestamp, 'Let’s inspire the new generation of explorers!', 10),
       (95, current_timestamp, 'Will we see human settlement on Mars?', 10),
       (96, current_timestamp, 'Space travel is becoming feasible!', 10),
       (97, current_timestamp, 'Curious about the technology behind it.', 10),
       (98, current_timestamp, 'Fascinating to think about life beyond Earth!', 10),
       (99, current_timestamp, 'Let’s invest in space sciences!', 10),
       (100, current_timestamp, 'Hope for successful missions ahead!', 10),

-- Comments for News ID 11
       (101, current_timestamp, 'New study on climate change is eye-opening!', 11),
       (102, current_timestamp, 'We need more action based on these findings.', 11),
       (103, current_timestamp, 'What can we do to help the environment?', 11),
       (104, current_timestamp, 'Let’s make sustainable choices together.', 11),
       (105, current_timestamp, 'Great article! More people need to be aware.', 11),
       (106, current_timestamp, 'The effects of climate change are serious.', 11),
       (107, current_timestamp, 'Hope to see policy changes soon!', 11),
       (108, current_timestamp, 'Raising awareness is crucial!', 11),
       (109, current_timestamp, 'Education is key to solving this issue.', 11),
       (110, current_timestamp, 'What actions can businesses take?', 11),

-- Comments for News ID 12
       (111, current_timestamp, 'The gaming industry is evolving fast!', 12),
       (112, current_timestamp, 'Excited to see new technologies in gaming!', 12),
       (113, current_timestamp, 'What new games are you all looking forward to?', 12),
       (114, current_timestamp, 'It’s a great time to be a gamer!', 12),
       (115, current_timestamp, 'The graphics look incredible!', 12),
       (116, current_timestamp, 'Let’s not forget about indie games!', 12),
       (117, current_timestamp, 'Any thoughts on VR gaming?', 12),
       (118, current_timestamp, 'Looking forward to upcoming releases!', 12),
       (119, current_timestamp, 'The community is so passionate!', 12),
       (120, current_timestamp, 'Gaming brings us all together.', 12),

-- Comments for News ID 13
       (121, current_timestamp, 'Medical breakthroughs are always exciting!', 13),
       (122, current_timestamp, 'Hope this leads to better treatments.', 13),
       (123, current_timestamp, 'What are the implications for public health?', 13),
       (124, current_timestamp, 'Great news for researchers!', 13),
       (125, current_timestamp, 'Let’s invest in more research!', 13),
       (126, current_timestamp, 'Every step towards better health is important.', 13),
       (127, current_timestamp, 'How will this affect healthcare costs?', 13),
       (128, current_timestamp, 'Patient care is a top priority.', 13),
       (129, current_timestamp, 'Let’s discuss the ethical implications.', 13),
       (130, current_timestamp, 'We need to keep following this story.', 13),

-- Comments for News ID 14
       (131, current_timestamp, 'What a powerful story!', 14),
       (132, current_timestamp, 'Inspiring to see such resilience.', 14),
       (133, current_timestamp, 'Let’s support these causes!', 14),
       (134, current_timestamp, 'We must amplify these voices.', 14),
       (135, current_timestamp, 'Such a timely message for everyone.', 14),
       (136, current_timestamp, 'What can we do to help?', 14),
       (137, current_timestamp, 'Hope to see more stories like this.', 14),
       (138, current_timestamp, 'Change starts with awareness.', 14),
       (139, current_timestamp, 'This is such important work.', 14),
       (140, current_timestamp, 'Real stories make a difference.', 14),

-- Comments for News ID 15
       (141, current_timestamp, 'Fantastic initiative for literacy!', 15),
       (142, current_timestamp, 'Education opens doors for everyone.', 15),
       (143, current_timestamp, 'What a great way to support the community!', 15),
       (144, current_timestamp, 'Let’s get involved and make a difference!', 15),
       (145, current_timestamp, 'This is how we build a better future.', 15),
       (146, current_timestamp, 'Love to see such positive changes!', 15),
       (147, current_timestamp, 'Let’s challenge ourselves to read more.', 15),
       (148, current_timestamp, 'Every child deserves access to books!', 15),
       (149, current_timestamp, 'Collecting books for those in need!', 15),
       (150, current_timestamp, 'Knowledge is power.', 15),

-- Comments for News ID 16
       (151, current_timestamp, 'Social media trends are fascinating!', 16),
       (152, current_timestamp, 'What’s next in social media?', 16),
       (153, current_timestamp, 'How does this impact real life interactions?', 16),
       (154, current_timestamp, 'Is privacy at risk with these trends?', 16),
       (155, current_timestamp, 'Looking forward to the future of communication!', 16),
       (156, current_timestamp, 'Excited to see how platforms evolve.', 16),
       (157, current_timestamp, 'Social media brings us together!', 16),
       (158, current_timestamp, 'Let’s talk about the negatives as well.', 16),
       (159, current_timestamp, 'We need to manage our online presence better.', 16),
       (160, current_timestamp, 'This topic is ever-evolving!', 16),

-- Comments for News ID 17
       (161, current_timestamp, 'Healthcare advancements are amazing!', 17),
       (162, current_timestamp, 'Excited for future innovations!', 17),
       (163, current_timestamp, 'How will these treatments be made accessible?', 17),
       (164, current_timestamp, 'We need comprehensive healthcare for all.', 17),
       (165, current_timestamp, 'Great news for patients!', 17),
       (166, current_timestamp, 'Let’s focus on preventive care.', 17),
       (167, current_timestamp, 'There’s always room for improvement.', 17),
       (168, current_timestamp, 'Will these be affordable for everyone?', 17),
       (169, current_timestamp, 'Cheers to the researchers making this happen!', 17),
       (170, current_timestamp, 'We must keep advocating for better healthcare.', 17),

-- Comments for News ID 18
       (171, current_timestamp, 'Fantastic achievements in sports!', 18),
       (172, current_timestamp, 'I love following these athletes!', 18),
       (173, current_timestamp, 'What a thrilling season it’s been!', 18),
       (174, current_timestamp, 'Talent and hard work shine through!', 18),
       (175, current_timestamp, 'Love the spirit of competition!', 18),
       (176, current_timestamp, 'Hope to see new records this year!', 18),
       (177, current_timestamp, 'What a game – worth the watch!', 18),
       (178, current_timestamp, 'Athletes inspire us all!', 18),
       (179, current_timestamp, 'Let’s keep cheering for our teams!', 18),
       (180, current_timestamp, 'Sport unites us all.', 18),

-- Comments for News ID 19
       (181, current_timestamp, 'Cultural events bring communities together!', 19),
       (182, current_timestamp, 'Let’s celebrate our diversity!', 19),
       (183, current_timestamp, 'What a beautiful showcase of talent!', 19),
       (184, current_timestamp, 'It’s amazing what art can do!', 19),
       (185, current_timestamp, 'We need more arts in our community.', 19),
       (186, current_timestamp, 'Bravo to all the performers!', 19),
       (187, current_timestamp, 'What creativity displayed here!', 19),
       (188, current_timestamp, 'Amazing to see our heritage celebrated!', 19),
       (189, current_timestamp, 'Let’s continue to support local artists!', 19),
       (190, current_timestamp, 'These events enrich our lives.', 19),

-- Comments for News ID 20
       (191, current_timestamp, 'Future of transportation is incredibly exciting!', 20),
       (192, current_timestamp, 'Let’s innovate for a better planet!', 20),
       (193, current_timestamp, 'What sustainable options are available?', 20),
       (194, current_timestamp, 'Bridging technology and eco-friendliness.', 20),
       (195, current_timestamp, 'Can’t wait for the electric cars!', 20),
       (196, current_timestamp, 'Let’s make traveling smarter!', 20),
       (197, current_timestamp, 'What a great opportunity for innovation!', 20),
       (198, current_timestamp, 'Transportation is evolving so quickly!', 20),
       (199, current_timestamp, 'Hope for a cleaner future.', 20),
       (200, current_timestamp, 'We can change the world through transport.', 20);