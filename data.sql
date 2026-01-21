COPY public.hospital (id, name) FROM stdin;
1	KCCG
2	KCSR
3	KCHR
4	KCBA
\.


--
-- TOC entry 4909 (class 0 OID 29641)
-- Dependencies: 218
-- Data for Name: lab_machine; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lab_machine (reagent_units, id, under_maintenance) FROM stdin;
500	4	f
500	3	f
500	1	f
500	5	f
480	2	f
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
-- TOC entry 4911 (class 0 OID 29647)
-- Dependencies: 220
-- Data for Name: queue; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.queue (active, hospital_id, id) FROM stdin;
f	2	2
f	3	3
f	4	4
f	1	1
\.


--
-- TOC entry 4913 (class 0 OID 29655)
-- Dependencies: 222
-- Data for Name: queue_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.queue_entry (id, patient_id, queue_id, type_id) FROM stdin;
\.


--
-- TOC entry 4915 (class 0 OID 29661)
-- Dependencies: 224
-- Data for Name: submit_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.submit_type (priority, id, name) FROM stdin;
t	1	Walk-in
f	2	Hospital
\.


--
-- TOC entry 4917 (class 0 OID 29667)
-- Dependencies: 226
-- Data for Name: technician; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.technician (is_busy, hospital_id, id, machine_id, user_id) FROM stdin;
f	3	15	15	17
f	3	13	13	15
f	3	11	11	13
f	4	16	16	18
f	4	17	17	19
f	2	10	10	12
f	3	12	12	14
f	3	14	14	16
f	1	4	4	6
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
-- TOC entry 4919 (class 0 OID 29675)
-- Dependencies: 228
-- Data for Name: test; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.test (finished, id, machine_id, patient_id, submit_type_id, technician_id, type_id) FROM stdin;
\.


--
-- TOC entry 4921 (class 0 OID 29681)
-- Dependencies: 230
-- Data for Name: test_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.test_type (duration, reagent_units_needed, id, name) FROM stdin;
60	20	1	Blood Test
90	30	2	Urine Test
180	100	3	PCR Test
240	150	4	Allergy Panel
\.