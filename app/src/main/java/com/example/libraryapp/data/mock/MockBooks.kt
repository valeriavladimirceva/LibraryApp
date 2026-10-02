package com.example.libraryapp.data.mock

import com.example.libraryapp.data.model.Book

object MockBooks{
    val books: List<Book> =listOf(
        Book(
            id = "OL15626917W",
            title = "The Lord of the Rings",
            authors = listOf("J. R. R. Tolkien"),
            coverId = 14625765,
            firstPublishYear = 1954,
            publishers = listOf("Allen & Unwin", "Houghton Mifflin"),
            isbn = "9780618640157",
            description = "The Lord of the Rings is an epic high-fantasy novel by English " +
                    "author and scholar J. R. R. Tolkien. Set in Middle-earth, the story began " +
                    "as a sequel to Tolkien's 1937 children's book The Hobbit, but eventually " +
                    "developed into a much larger work. Written in stages between 1937 and 1949.",
            subjects = listOf("Fantasy", "Adventure", "Classic", "Epic", "Fiction")
        ),
        Book(
            id = "OL27448W",
            title = "The Hobbit",
            authors = listOf("J. R. R. Tolkien"),
            coverId = 14627521,
            firstPublishYear = 1937,
            publishers = listOf("George Allen & Unwin"),
            isbn = "9780261102217",
            description = "The Hobbit, or There and Back Again is a children's fantasy " +
                    "novel by English author J. R. R. Tolkien. It was published in 1937 to " +
                    "wide critical acclaim, being nominated for the Carnegie Medal and awarded " +
                    "a prize from the New York Herald Tribune for best juvenile fiction.",
            subjects = listOf("Fantasy", "Children's", "Adventure", "Classic")
        ),
        Book(
            id = "OL82563W",
            title = "Harry Potter and the Philosopher's Stone",
            authors = listOf("J. K. Rowling"),
            coverId = 10523904,
            firstPublishYear = 1997,
            publishers = listOf("Bloomsbury"),
            isbn = "9780747532699",
            description = "Harry Potter and the Philosopher's Stone is a fantasy novel " +
                    "written by British author J. K. Rowling. The first novel in the Harry " +
                    "Potter series and Rowling's debut novel, it follows Harry Potter, a young " +
                    "wizard who discovers his magical heritage on his eleventh birthday.",
            subjects = listOf("Fantasy", "Young Adult", "Magic", "Adventure")
        ),
        Book(
            id = "OL7353617M",
            title = "Dune",
            authors = listOf("Frank Herbert"),
            coverId = 145747,
            firstPublishYear = 1965,
            publishers = listOf("Chilton Books"),
            isbn = "9780441172719",
            description = "Dune is a 1965 science fiction novel by American author Frank " +
                    "Herbert, originally published as two separate serials in Analog magazine. " +
                    "It tied with Roger Zelazny's This Immortal for the Hugo Award in 1966 and " +
                    "it won the inaugural Nebula Award for Best Novel.",
            subjects = listOf("Science Fiction", "Space Opera", "Classic", "Politics")
        ),
        Book(
            id = "OL45804W",
            title = "Fahrenheit 451",
            authors = listOf("Ray Bradbury"),
            coverId = 9271781,
            firstPublishYear = 1953,
            publishers = listOf("Ballantine Books"),
            isbn = "9781451673319",
            description = "Fahrenheit 451 is a 1953 dystopian novel by American writer " +
                    "Ray Bradbury. It presents an American society where books have been " +
                    "outlawed and 'firemen' burn any that are found. The novel is about " +
                    "the conflict between the protagonist Guy Montag and society.",
            subjects = listOf("Dystopia", "Science Fiction", "Classic", "Political")
        ),
        Book(
            id = "OL243885W",
            title = "1984",
            authors = listOf("George Orwell"),
            coverId = 9267242,
            firstPublishYear = 1949,
            publishers = listOf("Secker & Warburg"),
            isbn = "9780451524935",
            description = "Nineteen Eighty-Four: A Novel, often published as 1984, is a " +
                    "dystopian social science fiction novel by English novelist George Orwell. " +
                    "It was published on 8 June 1949 as Orwell's ninth and final book completed " +
                    "in his lifetime. Thematically, it centres on the consequences of " +
                    "totalitarianism, mass surveillance, and repressive regimentation.",
            subjects = listOf("Dystopia", "Classic", "Political", "Science Fiction")
        ),
        Book(
            id = "OL1168083W",
            title = "The Great Gatsby",
            authors = listOf("F. Scott Fitzgerald"),
            coverId = 8437623,
            firstPublishYear = 1925,
            publishers = listOf("Charles Scribner's Sons"),
            isbn = "9780743273565",
            description = "The Great Gatsby is a 1925 novel by American writer F. Scott " +
                    "Fitzgerald. Set in the Jazz Age on Long Island, near New York City, the " +
                    "novel depicts narrator Nick Carraway's interactions with mysterious " +
                    "millionaire Jay Gatsby and Gatsby's obsession to reunite with his former " +
                    "lover, Daisy Buchanan.",
            subjects = listOf("Classic", "Fiction", "Romance", "American Literature")
        ),
        Book(
            id = "OL81230W",
            title = "Pride and Prejudice",
            authors = listOf("Jane Austen"),
            coverId = 14348606,
            firstPublishYear = 1813,
            publishers = listOf("T. Egerton"),
            isbn = "9780141439518",
            description = "Pride and Prejudice is an 1813 romantic novel of manners " +
                    "written by Jane Austen. The novel follows the character development of " +
                    "Elizabeth Bennet, the dynamic protagonist of the book who learns about " +
                    "the repercussions of hasty judgments and comes to appreciate the " +
                    "difference between superficial goodness and actual goodness.",
            subjects = listOf("Romance", "Classic", "Fiction", "British Literature")
        ),
        Book(
            id = "OL46286W",
            title = "To Kill a Mockingbird",
            authors = listOf("Harper Lee"),
            coverId = 10674621,
            firstPublishYear = 1960,
            publishers = listOf("J. B. Lippincott & Co."),
            isbn = "9780061120084",
            description = "To Kill a Mockingbird is a novel by the American author Harper " +
                    "Lee. It was published in 1960 and was instantly successful. In the " +
                    "United States, it is widely read in high schools and middle schools. " +
                    "The plot and characters are loosely based on Lee's observations of her " +
                    "family, her neighbours and an event that occurred near her hometown in 1936.",
            subjects = listOf("Classic", "Fiction", "Coming of Age", "Drama", "Race")
        ),
        Book(
            id = "OL36743W",
            title = "The Catcher in the Rye",
            authors = listOf("J. D. Salinger"),
            coverId = 8243895,
            firstPublishYear = 1951,
            publishers = listOf("Little, Brown and Company"),
            isbn = "9780316769488",
            description = "The Catcher in the Rye is a novel by J. D. Salinger, partially " +
                    "published in serial form in 1945–1946 and as a novel in 1951. It was " +
                    "originally published for adults, but has become popular among adolescent " +
                    "readers for its themes of teenage angst and alienation.",
            subjects = listOf("Classic", "Fiction", "Coming of Age", "American Literature")
        ),
        Book(
            id = "OL102749W",
            title = "Brave New World",
            authors = listOf("Aldous Huxley"),
            coverId = 8776298,
            firstPublishYear = 1932,
            publishers = listOf("Chatto & Windus"),
            isbn = "9780060850524",
            description = "Brave New World is a dystopian social science fiction novel by " +
                    "English author Aldous Huxley, written in 1931 and published in 1932. " +
                    "Largely set in a futuristic World State, whose citizens are environmentally " +
                    "engineered into an intelligence-based social hierarchy.",
            subjects = listOf("Dystopia", "Science Fiction", "Classic", "Philosophy")
        ),
        Book(
            id = "OL153648W",
            title = "The Chronicles of Narnia",
            authors = listOf("C. S. Lewis"),
            coverId = 10502668,
            firstPublishYear = 1950,
            publishers = listOf("Geoffrey Bles", "HarperCollins"),
            isbn = "9780066238500",
            description = "The Chronicles of Narnia is a series of seven portal fantasy " +
                    "novels by C. S. Lewis. It is considered a classic of children's literature " +
                    "and is the author's best-known work. The series is set in the fictional " +
                    "realm of Narnia, a land of magic and mythical beasts.",
            subjects = listOf("Fantasy", "Children's", "Adventure", "Classic", "Christian")
        )
    )

    fun findById(id: String): Book? = books.firstOrNull { it.id == id}
}