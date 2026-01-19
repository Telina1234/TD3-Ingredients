
INSERT INTO dish (id, name, dish_type, selling_price) VALUES
                                                          (1, 'Salade fraîche', 'STARTER', 3500),
                                                          (2, 'Poulet grillé', 'MAIN', 12000),
                                                          (3, 'Riz aux légumes', 'MAIN', NULL),
                                                          (4, 'Gâteau au chocolat', 'DESSERT', 8000),
                                                          (5, 'Salade de fruits', 'DESSERT', NULL);

INSERT INTO ingredient (id, name, price, category) VALUES
                                                       (1, 'Laitue', 800, 'VEGETABLE'),
                                                       (2, 'Tomate', 600, 'VEGETABLE'),
                                                       (3, 'Poulet', 4500, 'ANIMAL'),
                                                       (4, 'Chocolat', 3000, 'OTHER'),
                                                       (5, 'Beurre', 2500, 'DAIRY');

INSERT INTO dish_ingredient (id, id_dish, id_ingredient, quantity_required, unit) VALUES
                                                                                      (1, 1, 1, 0.20, 'KG'),
                                                                                      (2, 1, 2, 0.15, 'KG'),
                                                                                      (3, 2, 3, 1.00, 'KG'),
                                                                                      (4, 4, 4, 0.30, 'KG'),
                                                                                      (5, 4, 5, 0.20, 'KG');
