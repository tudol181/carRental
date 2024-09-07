let reviews = /*[[${reviews}]]*/ []; // Thymeleaf will replace this with the list of reviews
let currentIndex = 5; // Initially showing 5 reviews

function loadMoreReviews() {
    // Get the container where reviews are displayed
    const reviewsContainer = document.getElementById('reviews-container');

    // Loop to add the next 5 reviews
    for (let i = currentIndex; i < currentIndex + 5 && i < 20; i++) {
        let review = reviews[i];

        // Create a new div for each review
        let reviewDiv = document.createElement('div');
        reviewDiv.className = "review-item border rounded p-3 mb-3";

        // Add the review comment
        let commentP = document.createElement('p');
        commentP.textContent = review.comment;
        reviewDiv.appendChild(commentP);

        // Add the reviewer's username
        let reviewerP = document.createElement('p');
        reviewerP.className = "text-muted";
        reviewerP.textContent = "Reviewed by: " + review.user.userName;
        reviewDiv.appendChild(reviewerP);

        // Append the review to the container
        reviewsContainer.appendChild(reviewDiv);
    }

    // Update the current index
    currentIndex += 5;

    // Hide the "Show More" button if there are no more reviews
    if (currentIndex >= reviews.length) {
        document.getElementById('show-more-btn').style.display = 'none';
    }
}