package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class IMDBGUI {
    private final JFrame mainFrame;
    private JPanel loginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    private JPanel actorsPanel;
    private JPanel headerPanel;
    private ImageIcon menuIcon;
    private JPanel homePanel;
    private JComboBox<String> genreComboBox;
    private JTextField searchField;

    private JList<String> recommendationList;
    JPanel centerPanel = new JPanel();
    JButton menuButton ;
    private JTextField minReviewsField;
    private JButton searchButton;
    private User authenticatedUser;
    public IMDBGUI() {
        // Initialize the main frame
        mainFrame = new JFrame("IMDb Application");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(600, 400); // You can adjust the size as needed
        menuIcon = new ImageIcon("C:\\Users\\User1\\Downloads\\kisspng-hamburger-button-computer-icons-menu-number-list-5b5c089faa0090.6298290015327581756963.png");
        initializeHeaderPanel();
        // Initialize and set up the login panel
        initializeLoginPanel();
        // Initialize the home panel (but do not display it yet)
        initializeHomePanel();
        // Set the login panel as the initial view
        mainFrame.add(loginPanel);
        initializeActorsPanel();
        // Make the main frame visible
        mainFrame.setVisible(true);
    }

    private void initializeHeaderPanel() {
        if(headerPanel == null) {
            headerPanel = new JPanel();
            headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            // Initialize the IMDb icon
            JButton imdbIcon = createImdbIcon();
            headerPanel.add(imdbIcon);

            // Initialize the search field
            searchField = new JTextField(20);
            searchField.setMaximumSize(searchField.getPreferredSize());

            // Initialize the search button
            searchButton = new JButton("Search");
            searchButton.addActionListener(e -> searchAll(searchField.getText()));

            // Initialize the menu button and its popup menu
            menuButton = createMenuButton(); // Assume this method creates the menu button and popup
            headerPanel.add(menuButton);

        }

    }

    private JButton createMenuButton() {
        ImageIcon scaledMenuIcon = new ImageIcon(new ImageIcon(menuIcon.getImage()).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        JButton menuButton = new JButton(scaledMenuIcon);
        menuButton.setPreferredSize(new Dimension(searchButton.getPreferredSize()));
        return menuButton;
    }

    private JPanel createHeaderPanel() {
        if (headerPanel == null) {
            headerPanel = new JPanel();
            headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));

            headerPanel.add(createImdbIcon(), 0);
            // Initialize the search field
            searchField = new JTextField(20);
            searchField.setMaximumSize(searchField.getPreferredSize());

            // Initialize the search button
            searchButton = new JButton("Search");
            searchButton.addActionListener(e -> searchAll(searchField.getText()));

            // Initialize the menu button and its popup menu
            menuButton = new JButton("Menu");

            // Add the search field, button, and menu to the header panel
            headerPanel.add(searchField);
            headerPanel.add(searchButton);
            headerPanel.add(menuButton);
        }
        return headerPanel;
    }
    private Image scaledBackgroundImage;
    private List<Production> favoriteProductions = new ArrayList<>();
    private Production currentSelectedProduction;
    private void createPopupMenu() {
        JPopupMenu menuPopup = new JPopupMenu();

        JMenuItem viewAllProductionsItem = new JMenuItem("View all productions");
        viewAllProductionsItem.addActionListener(e -> displayAllProductions());
        menuButton.addActionListener(e -> {
            menuPopup.show(menuButton, 0, menuButton.getHeight());
        });


        JMenuItem viewNotificationsItem = new JMenuItem("View received notifications");


        JMenuItem favoritesItem = new JMenuItem("Favorites");
        favoritesItem.addActionListener(e -> displayFavorites());

        menuPopup.add(viewAllProductionsItem);
        menuPopup.add(viewNotificationsItem);
        menuPopup.add(favoritesItem);

        menuButton.addActionListener(e -> menuPopup.show(menuButton, 0, menuButton.getHeight()));

    }

    private void initializeHomePanel() {
        final ImageIcon backgroundImage = new ImageIcon("C:\\Users\\User1\\Image\\v830-sasi-29.jpg");
        homePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (scaledBackgroundImage == null || shouldRescaleImage(this.getWidth(), this.getHeight())) {
                    scaledBackgroundImage = backgroundImage.getImage().getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
                }
                g.drawImage(scaledBackgroundImage, 0, 0, this);
            }

            private boolean shouldRescaleImage(int width, int height) {
                return scaledBackgroundImage == null ||
                        scaledBackgroundImage.getWidth(null) != width ||
                        scaledBackgroundImage.getHeight(null) != height;
            }
        };
        homePanel.setOpaque(false);
        homePanel.add(createHeaderPanel(), BorderLayout.NORTH);
        homePanel.setVisible(false);


        // Create a panel for the search bar and set it to the top
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.X_AXIS));
        searchBarPanel.setOpaque(false);
        homePanel.add(searchBarPanel, BorderLayout.NORTH);
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Recommendations List inside a scroll pane
        List<Production> productionList = IMDB.getInstance().getProductions();
        String[] recommendations = productionList.stream()
                .map(Production::getTitle)
                .toArray(String[]::new);

        recommendationList = new JList<>(recommendations);
        JScrollPane scrollPane = new JScrollPane(recommendationList);
        recommendationList.setSelectionBackground(new Color(0, 0, 0, 0));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(scrollPane);
        scrollPane.setOpaque(false);

        // Genre Filter
        genreComboBox = new JComboBox<>(getGenres());
        genreComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(genreComboBox);
        genreComboBox.addActionListener(e -> {
            String selectedGenreName = (String) genreComboBox.getSelectedItem();
            Genre selectedGenre = null;
            if (!"All Genres".equals(selectedGenreName)) {
                selectedGenre = Genre.valueOf(selectedGenreName);
            }
            updateRecommendations(selectedGenre);

        });

        homePanel.add(centerPanel, BorderLayout.CENTER);

        // Add the search bar components to the searchBarPanel, not the homePanel directly
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        searchBarPanel.add(searchField); // Add to searchBarPanel
        searchBarPanel.add(searchButton); // Add to searchBarPanel
        menuButton = new JButton("Menu");
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchButton.addActionListener(e -> {
            String query = searchField.getText();
            searchAll(query);
        });

        // Create the bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        //LoggingOut button
        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(e -> switchToLoginPanel());
        bottomPanel.add(logoutButton);

        // Button to add the selected production to the favorites
        JButton addProductionToFavoritesButton = new JButton("Add Production to Favorites");
        addProductionToFavoritesButton.addActionListener(e -> {
            if (currentSelectedProduction != null && !favoriteProductions.contains(currentSelectedProduction)) {
                favoriteProductions.add(currentSelectedProduction);
                JOptionPane.showMessageDialog(homePanel, currentSelectedProduction.getTitle() + " has been added to your favorites!");
            } else {
                JOptionPane.showMessageDialog(homePanel, "No production selected or it's already in favorites.");
            }
        });


        recommendationList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedTitle = recommendationList.getSelectedValue();
                currentSelectedProduction = productionList.stream()
                        .filter(production -> production.getTitle().equals(selectedTitle))
                        .findFirst()
                        .orElse(null);
                viewProductionDetails(selectedTitle);
            }
        });


        // Add buttons to the bottom panel
        bottomPanel.add(addProductionToFavoritesButton);

        //Actors page button
        JButton actorsPageButton = new JButton("Actors Page");
        actorsPageButton.addActionListener(e -> switchToActorsPanel());
        bottomPanel.add(actorsPageButton);

        homePanel.add(bottomPanel, BorderLayout.SOUTH);


        // POPUP MENU
        createPopupMenu();

        menuIcon = new ImageIcon("C:\\Users\\User1\\Downloads\\kisspng-hamburger-button-computer-icons-menu-number-list-5b5c089faa0090.6298290015327581756963.png"); // Replace with the path to your menu icon
        Image menuImage = menuIcon.getImage();
        Image newMenuImg = menuImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        menuButton.setIcon(new ImageIcon(newMenuImg));
        menuButton.setPreferredSize(new Dimension(searchButton.getPreferredSize()));

        searchBarPanel.add(menuButton);

        homePanel.setVisible(true);
        homePanel.revalidate();
        homePanel.repaint();

    }


    private void displayFavorites() {
        JDialog favoritesDialog = new JDialog(mainFrame, "Favorites", true);
        favoritesDialog.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        DefaultListModel<Actor> favoriteActorsModel = new DefaultListModel<>();
        favoriteActors.forEach(favoriteActorsModel::addElement);
        JList<Actor> favoriteActorsList = new JList<>(favoriteActorsModel);
        JScrollPane actorsScrollPane = new JScrollPane(favoriteActorsList);
        JButton removeActorButton = new JButton("Remove Selected Actor");
        removeActorButton.addActionListener(e -> {
            Actor selectedActor = favoriteActorsList.getSelectedValue();
            if (selectedActor != null) {
                favoriteActorsModel.removeElement(selectedActor);
                favoriteActors.remove(selectedActor);
            }
        });

        // Panel for actors tab
        JPanel actorsPanel = new JPanel(new BorderLayout());
        actorsPanel.add(actorsScrollPane, BorderLayout.CENTER);
        actorsPanel.add(removeActorButton, BorderLayout.SOUTH);
        tabbedPane.addTab("Actors", actorsPanel);

        // Tab for favorite productions
        DefaultListModel<Production> favoritesModel = new DefaultListModel<>();
        favoriteProductions.forEach(favoritesModel::addElement);
        JList<Production> favoritesList = new JList<>(favoritesModel);
        JScrollPane productionsScrollPane = new JScrollPane(favoritesList);
        JButton removeProductionButton = new JButton("Remove Selected Production");
        removeProductionButton.addActionListener(e -> {
            Production selectedProduction = favoritesList.getSelectedValue();
            if (selectedProduction != null) {
                favoritesModel.removeElement(selectedProduction);
                favoriteProductions.remove(selectedProduction);
            }
        });

        // Panel for productions tab
        JPanel productionsPanel = new JPanel(new BorderLayout());
        productionsPanel.add(productionsScrollPane, BorderLayout.CENTER);
        productionsPanel.add(removeProductionButton, BorderLayout.SOUTH);
        tabbedPane.addTab("Productions", productionsPanel);

        favoritesDialog.add(tabbedPane, BorderLayout.CENTER);

        // Close button for the dialog
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> favoritesDialog.dispose());
        favoritesDialog.add(closeButton, BorderLayout.SOUTH);

        favoritesDialog.setSize(400, 500); // Adjust size as needed
        favoritesDialog.setLocationRelativeTo(mainFrame);
        favoritesDialog.setVisible(true);
    }


    private void viewProductionDetails(String title) {

        Production selectedProduction = IMDB.getInstance().getProductionByName(title);


        JDialog detailsDialog = new JDialog(mainFrame, "Production Details", true);
        detailsDialog.setLayout(new BorderLayout());

        // Add the production details to the dialog
        JTextArea detailsTextArea = new JTextArea(10, 30);
        detailsTextArea.setEditable(false);
        detailsTextArea.setText(selectedProduction.toString());

        detailsDialog.add(new JScrollPane(detailsTextArea), BorderLayout.CENTER);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Add a button to add a review
        JButton addReviewButton = new JButton("Add Review");
        addReviewButton.addActionListener(e -> addReview(title));
        buttonPanel.add(addReviewButton);

        // Add a button to close the dialog
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> detailsDialog.dispose());
        buttonPanel.add(closeButton);

        // Add the button panel to the dialog
        detailsDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Pack and show the dialog
        detailsDialog.pack();
        detailsDialog.setLocationRelativeTo(mainFrame);
        detailsDialog.setVisible(true);


    }

    private void addReview(String title) {
        JDialog reviewDialog = new JDialog(mainFrame, "Add Review", true);
        reviewDialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);

        JTextField titleField = new JTextField(title);
        titleField.setEditable(false);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        reviewDialog.add(new JLabel("Title:"), c);
        c.gridx = 1;
        c.weightx = 0.9;
        reviewDialog.add(titleField, c);

        JTextField ratingField = new JTextField(5);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
        reviewDialog.add(new JLabel("Rating (1-10):"), c);
        c.gridx = 1;
        c.weightx = 0.9;
        reviewDialog.add(ratingField, c);

        // Comment input
        JTextArea commentArea = new JTextArea(5, 20);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.1;
        reviewDialog.add(new JLabel("Comment:"), c);
        c.gridx = 1;
        c.weightx = 0.9;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        reviewDialog.add(new JScrollPane(commentArea), c);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                int ratingValue = Integer.parseInt(ratingField.getText());
                String comment = commentArea.getText();
                reviewDialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(reviewDialog, "Please enter a valid rating.");
            }
        });
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        reviewDialog.add(submitButton, c);

        reviewDialog.pack();
        reviewDialog.setLocationRelativeTo(mainFrame);
        reviewDialog.setVisible(true);
    }

    private void updateRecommendationsList(List<Production> productions) {
        String[] titles = productions.stream()
                .map(Production::getTitle)
                .toArray(String[]::new);
        recommendationList.setListData(titles);
    }
    private JButton createImdbIcon() {
            ImageIcon imdbIconImage = new ImageIcon("C:\\Users\\User1\\Downloads\\kisspng-brand-imdb-logo-resume-manufacturing-5b55454e1489c9.8268339515323149580841.png");
            Image image = imdbIconImage.getImage();
            Image newimg = image.getScaledInstance(45, 45, Image.SCALE_SMOOTH); // Adjust size as needed
            JButton imdbIcon = new JButton(new ImageIcon(newimg));

            imdbIcon.setBorderPainted(false);
            imdbIcon.setContentAreaFilled(false);
            imdbIcon.setFocusPainted(false);
            imdbIcon.setOpaque(false);
            imdbIcon.addActionListener(e -> switchToHomePanel());

        return imdbIcon;
    }

    private void displayAllProductions() {
        JPanel allProductionsPanel = new JPanel(new BorderLayout());
        allProductionsPanel.add(createHeaderPanel(), BorderLayout.NORTH);

        // Create a model for the list
        DefaultListModel<String> model = new DefaultListModel<>();
        List<Production> productions = IMDB.getInstance().getProductions();
        productions.stream()
                .sorted(Comparator.comparing(Production::getTitle)) // Sort by title
                .forEach(production -> model.addElement(production.getTitle()));

        // Create the list and add it to a scroll pane
        JList<String> productionsList = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(productionsList);
        allProductionsPanel.add(scrollPane, BorderLayout.CENTER);

        // Replace the content of the mainFrame with the allProductionsPanel
        mainFrame.getContentPane().removeAll(); // Remove current panel
        mainFrame.getContentPane().add(allProductionsPanel); // Add the allProductionsPanel
        mainFrame.getContentPane().revalidate();
        mainFrame.getContentPane().repaint();
    }

    public void showHomePanel() {
        // Remove all current components
        mainFrame.getContentPane().removeAll();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());


        JLabel welcomeMessage = new JLabel("Welcome to IMDb! \n Press the icon to start!", SwingConstants.CENTER);
        welcomeMessage.setFont(new Font("Serif", Font.BOLD, 30));
        welcomeMessage.setForeground(Color.PINK);
        welcomeMessage.setPreferredSize(new Dimension(mainFrame.getWidth(), 50));

        // Add the welcome message to the top panel
        topPanel.add(welcomeMessage, BorderLayout.NORTH);

        // Add the headerPanel to the top panel
        topPanel.add(headerPanel, BorderLayout.SOUTH);

        // Add the topPanel to the content pane
        mainFrame.getContentPane().add(topPanel, BorderLayout.NORTH);


        mainFrame.getContentPane().revalidate();
        mainFrame.getContentPane().repaint();

        mainFrame.setVisible(true);
    }
    private void switchToHomePanel() {
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(homePanel);
        homePanel.setVisible(true);
        mainFrame.getContentPane().revalidate();
        mainFrame.getContentPane().repaint();
    }
    private void switchToActorsPanel() {
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(actorsPanel);
        mainFrame.getContentPane().revalidate();
        mainFrame.getContentPane().repaint();
    }

    private void switchToLoginPanel() {
        resetHomePanel();

        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(loginPanel);
        mainFrame.getContentPane().revalidate();
        mainFrame.getContentPane().repaint();
    }

    private void resetHomePanel() {
        favoriteActors.clear();
        favoriteProductions.clear();

    }
    private List<Actor> favoriteActors = new ArrayList<>();
    private Actor currentSelectedActor;

    private void initializeActorsPanel() {
        actorsPanel = new JPanel();
        actorsPanel.setLayout(new BorderLayout());

        DefaultListModel<String> actorsModel = new DefaultListModel<>();
        List<Actor> sortedActors = (List<Actor>) IMDB.getInstance().getActors().stream()
                .sorted(Comparator.comparing(Actor::getName))
                .collect(Collectors.toList());

        for (Actor actor : sortedActors) {
            actorsModel.addElement(actor.getName());
        }

        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> switchToHomePanel());

        JList<String> actorsList = new JList<>(actorsModel);
        JScrollPane scrollPane = new JScrollPane(actorsList);
        actorsPanel.add(scrollPane, BorderLayout.CENTER);

        JTextArea actorDetailsArea = new JTextArea(10, 20);
        actorDetailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(actorDetailsArea);
        actorsPanel.add(detailsScrollPane, BorderLayout.EAST);

        // Add to Favorites Button
        JButton addToFavoritesButton = new JButton("Add to Favorites");
        addToFavoritesButton.addActionListener(e -> {
            if (currentSelectedActor != null && !favoriteActors.contains(currentSelectedActor)) {
                favoriteActors.add(currentSelectedActor);
                JOptionPane.showMessageDialog(actorsPanel, currentSelectedActor.getName() + " has been added to your favorites!");
            }
        });


        actorsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedActorName = actorsList.getSelectedValue();
                currentSelectedActor = sortedActors.stream()
                        .filter(actor -> actor.getName().equals(selectedActorName))
                        .findFirst()
                        .orElse(null);

                if (currentSelectedActor != null) {
                    actorDetailsArea.setText(currentSelectedActor.toString());
                } else {
                    actorDetailsArea.setText("Actor details not found.");
                }
            }
        });


        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(addToFavoritesButton);
        buttonsPanel.add(backButton);

        actorsPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void customizeComponent(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        component.setMaximumSize(component.getPreferredSize());

    }

    private void customizeButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(button.getPreferredSize());
    }

    private void updateRecommendations(Genre genre) {
        List<Production> allProductions = IMDB.getInstance().getProductions();
        List<String> filteredTitles = new ArrayList<>();

        for (Production production : allProductions) {
            // Check if production's genres list contains the selected genre
            if ((genre == null || production.getGenres().contains(genre))) {
                filteredTitles.add(production.getTitle());
            }

        }

        // Convert the filtered list to an array and update the JList
        String[] recommendations = filteredTitles.toArray(new String[0]);
        recommendationList.setListData(recommendations);
    }

    private String[] getGenres() {
        Genre[] genres = Genre.values();
        String[] genreNames = new String[genres.length + 1];
        genreNames[0] = "All Genres"; // First option for all genres
        for (int i = 0; i < genres.length; i++) {
            genreNames[i + 1] = genres[i].toString();
        }
        return genreNames;
    }
    private void searchAll(String query) {

        query = query.toLowerCase().trim();

        DefaultListModel<String> model = new DefaultListModel<>();

        // If the query is "All Actors", add all actors to the search results
        if ("all actors".equals(query)) {
            for (Object actor : IMDB.getInstance().getActors()) {
                Actor actor1 = (Actor) actor;
                model.addElement(actor1.getName());
            }
        } else {
            // Include productions in the search results
            for (Object production : IMDB.getInstance().getProductions()) {
                Production production1 = (Production) production;
                if ((query.isEmpty() || production1.getTitle().toLowerCase().contains(query))) {
                    model.addElement(production1.getTitle());
                }
            }

            // Include actors in the search results
            for (Object actor : IMDB.getInstance().getActors()) {
                Actor actor1 = (Actor) actor;
                if (actor1.getName().toLowerCase().contains(query)) {
                    model.addElement(actor1.getName());
                }
            }
        }


        recommendationList.setModel(model);
    }

    private void initializeLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new OverlayLayout(loginPanel));
        loginPanel.setOpaque(false);


        ImageIcon originalBackgroundImage = new ImageIcon("C:\\Users\\User1\\Image\\v830-sasi-29.jpg");

        Image resizedBackgroundImage = originalBackgroundImage.getImage().getScaledInstance(
               600,
                900,
                Image.SCALE_SMOOTH
        );
        ImageIcon backgroundImage = new ImageIcon(resizedBackgroundImage);
        JLabel backgroundLabel = new JLabel(backgroundImage);

        JPanel backgroundPanel = new JPanel(new BorderLayout());
        backgroundPanel.add(backgroundLabel, BorderLayout.CENTER);


        loginPanel.add(backgroundPanel);

        JPanel componentsPanel = new JPanel();
        componentsPanel.setLayout(new BoxLayout(componentsPanel, BoxLayout.Y_AXIS));
        componentsPanel.setOpaque(false);

        componentsPanel.add(Box.createVerticalStrut(40)); // This adds a fixed amount of space

        // Add the IMDB logo
        ImageIcon logoIcon = new ImageIcon("C:\\Users\\User1\\Downloads\\Screenshot_2024-01-06_220636-removebg-preview.png"); // Replace with your logo path
        Image scaledImage = logoIcon.getImage().getScaledInstance(260, 240, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(resizedIcon);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the logo
        componentsPanel.add(logoLabel);

        componentsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel usernameLabel = new JLabel("Email:");
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setForeground(new Color(255, 255, 255));
        componentsPanel.add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setMaximumSize(usernameField.getPreferredSize());
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        componentsPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordLabel.setForeground(new Color(255,255,255));
        componentsPanel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(passwordField.getPreferredSize());
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        componentsPanel.add(passwordField);


        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        componentsPanel.add(loginButton);


        loginPanel.add(componentsPanel);

        loginPanel.setComponentZOrder(componentsPanel, 0);
        loginPanel.setComponentZOrder(backgroundPanel, 1);

        // Customize the components
        customizeComponent(usernameField);
        customizeComponent(passwordField);
        customizeButton(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (IMDB.getInstance().isValidUser(username, password)) {
                // Credentials are correct
                showHomePanel();
            } else {
                // Show error message
                JOptionPane.showMessageDialog(mainFrame, "Invalid credentials, please try again.",
                        "Login Error", JOptionPane.ERROR_MESSAGE);

                usernameField.setText("");
                passwordField.setText("");
            }
        });
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public JPanel getLoginPanel() {
        return loginPanel;
    }

    public void setLoginPanel(JPanel loginPanel) {
        this.loginPanel = loginPanel;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public void setUsernameField(JTextField usernameField) {
        this.usernameField = usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public JPanel getActorsPanel() {
        return actorsPanel;
    }

    public void setActorsPanel(JPanel actorsPanel) {
        this.actorsPanel = actorsPanel;
    }

    public JPanel getHeaderPanel() {
        return headerPanel;
    }

    public void setHeaderPanel(JPanel headerPanel) {
        this.headerPanel = headerPanel;
    }

    public ImageIcon getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(ImageIcon menuIcon) {
        this.menuIcon = menuIcon;
    }

    public JPanel getHomePanel() {
        return homePanel;
    }

    public void setHomePanel(JPanel homePanel) {
        this.homePanel = homePanel;
    }

    public JComboBox<String> getGenreComboBox() {
        return genreComboBox;
    }

    public void setGenreComboBox(JComboBox<String> genreComboBox) {
        this.genreComboBox = genreComboBox;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public void setSearchField(JTextField searchField) {
        this.searchField = searchField;
    }

    public JList<String> getRecommendationList() {
        return recommendationList;
    }

    public void setRecommendationList(JList<String> recommendationList) {
        this.recommendationList = recommendationList;
    }

    public JPanel getCenterPanel() {
        return centerPanel;
    }

    public void setCenterPanel(JPanel centerPanel) {
        this.centerPanel = centerPanel;
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    public void setMenuButton(JButton menuButton) {
        this.menuButton = menuButton;
    }

    public JTextField getMinReviewsField() {
        return minReviewsField;
    }

    public void setMinReviewsField(JTextField minReviewsField) {
        this.minReviewsField = minReviewsField;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(JButton searchButton) {
        this.searchButton = searchButton;
    }

    public Image getScaledBackgroundImage() {
        return scaledBackgroundImage;
    }

    public void setScaledBackgroundImage(Image scaledBackgroundImage) {
        this.scaledBackgroundImage = scaledBackgroundImage;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(IMDBGUI::new);
    }
}
