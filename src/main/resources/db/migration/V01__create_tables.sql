create TABLE public.categories (
	 id serial PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

create TABLE public.accountings (
	 id serial PRIMARY KEY,
    description VARCHAR(50) NOT NULL,
    payment_date DATE,
    due_date DATE,
    value NUMERIC(10, 2),
    category_id INTEGER,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
