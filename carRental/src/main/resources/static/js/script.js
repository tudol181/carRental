let reviews = /*[[${reviews}]]*/ []; // list of reviews
let currentIndex = 5; // 5 reviews

function loadMoreReviews() {
    //container where reviews are displayed
    const reviewsContainer = document.getElementById('reviews-container');

    //add the next 5 reviews
    for (let i = currentIndex; i < currentIndex + 5 && i < 20; i++) {
        let review = reviews[i];

        //new div for each review
        let reviewDiv = document.createElement('div');
        reviewDiv.className = "review-item border rounded p-3 mb-3";

        //add review comment
        let commentP = document.createElement('p');
        commentP.textContent = review.comment;
        reviewDiv.appendChild(commentP);

        //add username
        let reviewerP = document.createElement('p');
        reviewerP.className = "text-muted";
        reviewerP.textContent = "Reviewed by: " + review.user.userName;
        reviewDiv.appendChild(reviewerP);

        //add review to the container
        reviewsContainer.appendChild(reviewDiv);
    }

    //update the index
    currentIndex += 5;

    //hide button
    if (currentIndex >= reviews.length) {
        document.getElementById('show-more-btn').style.display = 'none';
    }
}