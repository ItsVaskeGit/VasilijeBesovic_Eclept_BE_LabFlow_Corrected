COPY public.lab_machine (reagent_units, id, under_maintenance) FROM stdin;
500	3	f
500	1	f
500	5	f
480	2	f
480	4	f
460	15	f
320	7	f
460	13	f
460	10	f
460	20	f
320	12	f
440	16	f
320	17	f
440	11	f
400	19	f
400	14	f
460	18	f
460	8	f
400	9	f
440	6	f
\.


--
-- TOC entry 4902 (class 0 OID 29647)
-- Dependencies: 220
-- Data for Name: queue; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.queue (active, hospital_id, id) FROM stdin;
f	2	2
f	3	3
f	4	4
t	1	1
\.


--
-- TOC entry 4904 (class 0 OID 29655)
-- Dependencies: 222
-- Data for Name: queue_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.queue_entry (id, patient_id, queue_id, type_id) FROM stdin;
\.


--
-- TOC entry 4906 (class 0 OID 29661)
-- Dependencies: 224
-- Data for Name: submit_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.submit_type (priority, id, name) FROM stdin;
t	1	Walk-in
f	2	Hospital
\.


--
-- TOC entry 4908 (class 0 OID 29667)
-- Dependencies: 226
-- Data for Name: technician; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.technician (is_busy, hospital_id, id, machine_id, user_id) FROM stdin;
t	1	4	4	6
f	3	15	15	17
f	3	13	13	15
f	3	11	11	13
f	4	16	16	18
f	4	17	17	19
f	2	10	10	12
f	3	12	12	14
f	3	14	14	16
f	1	3	3	5
f	1	1	1	3
f	4	19	19	21
f	2	8	8	10
f	1	5	5	7
f	2	9	9	11
f	1	2	2	4
f	4	20	20	22
f	2	7	7	9
f	4	18	18	20
f	2	6	6	8
\.


--
-- TOC entry 4910 (class 0 OID 29675)
-- Dependencies: 228
-- Data for Name: test; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.test (finished, id, machine_id, patient_id, submit_type_id, technician_id, type_id) FROM stdin;
f	94	4	3	\N	4	1
\.


--
-- TOC entry 4912 (class 0 OID 29681)
-- Dependencies: 230
-- Data for Name: test_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.test_type (duration, reagent_units_needed, id, name) FROM stdin;
60	20	1	Blood Test
90	30	2	Urine Test
180	100	3	PCR Test
240	150	4	Allergy Panel
\.


--
-- TOC entry 4914 (class 0 OID 29687)
-- Dependencies: 232
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (is_technician, id, password, username, roles) FROM stdin;
t	3	$2a$10$u1Wx6h3ZQByLncp7UaI0rOqJLbcOTisyppXZJWr1sG5pBNj7OEfve	neko1	user
t	4	$2a$10$gJ1iopW0L5hBz9lnm5IQQeWf/iJvy7nt.nMvg2Tq9bcvsXtsX69gC	neko2	user
t	5	$2a$10$a2vVxeea33fo9lsjZJpvVOhZdY6ttU2oTkXgWCN.9N9VIVHS3Tl.6	neko3	user
t	6	$2a$10$BG0YmDTF/VBNgPUztmEj..nJyvNFxRNuS2NPzZ.ssVxQG7yrEZ.gq	neko4	user
t	7	$2a$10$soY6E5ZLPoqXwucag3Xl9evj9xPAB.TiJxpvJf23C2jGrN9Aj9Mpa	neko5	user
t	8	$2a$10$TA9adY6KTtD.uFwZDUnQb.UVfW4KPwUGs2qm65M8OcpCEcJzS8VPi	neko6	user
t	9	$2a$10$.3LxnzU1/y53g/DPk3nYo.PYnwZUINweQWewTETHIIPr6L7S9feiu	neko7	user
t	10	$2a$10$sd13mQIsqeOCMs6UOWlAK.hr02egbMSwPYY5M18AUTaIfCJv9ZeOu	neko8	user
t	11	$2a$10$zwEz5h1sBDHClCGG0HwNBecETXeuwpBY97yjqYziE7b9oUzCGAzxy	neko9	user
t	12	$2a$10$WmlsDagyw/YVzSKNDYNwPer4n6D7LpxvVAxnv8kv8jWVcjd3u8wAO	neko10	user
t	13	$2a$10$8P7Lr9LMRc0xK9KnHsuRzeegkyRzJLBqQIVp0EoQG3.CFu0qlgWpu	neko11	user
t	14	$2a$10$rJrscBcBksjz.SNg3QRHgeDHCtGOIq84Ivtyhf0pYLLpXjwvkFsIm	neko12	user
t	15	$2a$10$15XWMoNrKmL0H7e2MoVKKe/C0yQdzOaG3Iww75acsmq1atIY51Q2C	neko13	user
t	16	$2a$10$fL7HXU0o.mier7TOn50fu.FS8hQS2LOxRL9Yl3QcnIcSzBYNqpyjq	neko14	user
t	17	$2a$10$KP63CAZntt0XPS6IKzz.a.bPIXhHZBTu/a6ntJO7yWmjV0U5tnbPi	neko15	user
t	18	$2a$10$GLLwEGN2yT7F5j0Ci0DCC.c41PdQIv2dM36KxA.6XC1gD6BM7MxyK	neko16	user
t	19	$2a$10$aSnbSF/xyibJNKHJtuaFXuBH5lJjb3cbafJtuJdvFuKsejii7qY4a	neko17	user
t	20	$2a$10$DECzitNeFxvtkQ0Q/3tg8esKZPMkeyMxIaPRVW7XI/CCNOE.VeNLS	neko18	user
t	21	$2a$10$MM2OU8ZYIgAmC0zpwuM.d.wyeTn3IZrI7Ijo/DCxcia5QzTiodeTW	neko19	user
t	22	$2a$10$9WWOrX7MT4ut1oyucWcVM.ZTW76YecX/81LwAo4Q04pp/fsUvnaLS	neko20	user
f	2	$2a$10$xreyi0EWFslNK9njNp4eAeHvuEjx9rwhfjqw4Mi69vitiXTEqD4c.	neko	admin
\.