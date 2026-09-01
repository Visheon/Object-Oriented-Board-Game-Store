package Store;

import java.util.List;

import CourseworkFiles.Product;


 // Defines search and filter operations on the product stock.

public interface Searchable {

    // Looks up a product by its unique 4-digit product ID.
	
    Product findById(int productId);


    // Filters accessories whose compatibility contains the given string.

    List<Accessory> filterByCompatibility(String compatibility);
}
