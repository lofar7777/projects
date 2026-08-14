import styles from "./Hero.module.css";

function Hero(){
    return(
        <section className={styles.hero}>
            <div className={styles.content}>
                <h1>Know Your Food</h1>
                    <p>
                        Understand what's inside your food before you eat it. 
                    </p>
                    <button className={styles.button}>
                        Get Started
                    </button>
            </div>
        </section>
    );
}

export default Hero;