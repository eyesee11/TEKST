// TEKST Retro Website JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // Matrix-style background effect
    createMatrixBackground();
    
    // Smooth scrolling for navigation links
    const navLinks = document.querySelectorAll('a[href^="#"]');
    
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href');
            const targetSection = document.querySelector(targetId);
            
            if (targetSection) {
                // Add retro transition effect
                addGlitchEffect(this);
                
                setTimeout(() => {
                    targetSection.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }, 200);
            }
        });
    });

    // Enhanced navbar scroll effect
    const navbar = document.querySelector('.navbar');
    let lastScrollTop = 0;
    let ticking = false;

    function updateNavbar() {
        const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        
        if (scrollTop > 100) {
            navbar.style.background = 'rgba(13, 17, 23, 0.98)';
            navbar.style.backdropFilter = 'blur(15px)';
        } else {
            navbar.style.background = 'rgba(13, 17, 23, 0.95)';
            navbar.style.backdropFilter = 'blur(10px)';
        }
        
        lastScrollTop = scrollTop <= 0 ? 0 : scrollTop;
        ticking = false;
    }

    window.addEventListener('scroll', function() {
        if (!ticking) {
            requestAnimationFrame(updateNavbar);
            ticking = true;
        }
    });

    // Enhanced Intersection Observer with retro animations
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                animateElementIn(entry.target);
            }
        });
    }, observerOptions);

    // Observe all feature cards, download cards, and doc cards
    const animatedElements = document.querySelectorAll('.feature-card, .download-card, .doc-card, .screenshot-item, .step');
    
    animatedElements.forEach((el, index) => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(40px) rotateX(15deg)';
        el.style.transition = 'all 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94)';
        el.style.transitionDelay = `${index * 0.1}s`;
        observer.observe(el);
    });

    function animateElementIn(element) {
        element.style.opacity = '1';
        element.style.transform = 'translateY(0) rotateX(0)';
        
        // Add retro glow effect
        element.style.boxShadow = '0 0 20px rgba(57, 255, 20, 0.2)';
        setTimeout(() => {
            element.style.boxShadow = '';
        }, 1000);
    }

    // Enhanced download tracking with retro effects
    const downloadButtons = document.querySelectorAll('.btn-primary, .btn-secondary, .btn-download');
    
    downloadButtons.forEach(button => {
        // Add retro hover sound effect (visual feedback)
        button.addEventListener('mouseenter', function() {
            this.style.textShadow = '0 0 10px currentColor';
        });
        
        button.addEventListener('mouseleave', function() {
            this.style.textShadow = '';
        });
        
        button.addEventListener('click', function(e) {
            const downloadType = this.textContent.trim();
            console.log(`Action initiated: ${downloadType}`);
            
            // Add retro click effect
            addRetroClickEffect(this, e);
            
            // If it's an actual download button
            if (this.href && this.href.includes('.jar')) {
                const originalText = this.innerHTML;
                this.innerHTML = '<span class="loading-dots">downloading</span>';
                this.style.pointerEvents = 'none';
                
                setTimeout(() => {
                    this.innerHTML = originalText;
                    this.style.pointerEvents = 'auto';
                }, 2000);
            }
        });
    });

    // Check Java version (optional feature)
    function checkJavaVersion() {
        try {
            // This is a simplified check - in reality, you'd need a more robust solution
            const userAgent = navigator.userAgent;
            const javaEnabled = navigator.javaEnabled && navigator.javaEnabled();
            
            if (!javaEnabled) {
                showJavaWarning();
            }
        } catch (error) {
            console.log('Java check not available in this browser');
        }
    }

    function showJavaWarning() {
        const warningDiv = document.createElement('div');
        warningDiv.className = 'java-warning';
        warningDiv.innerHTML = `
            <div style="background: #09172bff; border: 1px solid #ffeaa7; padding: 15px; margin: 20px; border-radius: 8px; text-align: center;">
                <strong>⚠️ Java Required:</strong> TEKST requires Java 21 or higher to run. 
                <a href="https://www.java.com/en/download/manual.jsp" target="_blank" style="color: #7CB342; text-decoration: underline;">Download Java here</a>
            </div>
        `;
        
        const downloadSection = document.getElementById('download');
        downloadSection.insertBefore(warningDiv, downloadSection.firstChild);
    }

    // Initialize Java check
    checkJavaVersion();

    // Add parallax effect to hero section
    const hero = document.querySelector('.hero');
    
    window.addEventListener('scroll', function() {
        const scrolled = window.pageYOffset;
        const parallaxSpeed = 0.5;
        
        if (hero) {
            hero.style.transform = `translateY(${scrolled * parallaxSpeed}px)`;
        }
    });

    // Add type writer effect to hero title (optional)
    function typeWriter(element, text, speed = 100) {
        let i = 0;
        element.innerHTML = '';
        
        function type() {
            if (i < text.length) {
                element.innerHTML += text.charAt(i);
                i++;
                setTimeout(type, speed);
            }
        }
        
        type();
    }

    // Initialize typewriter effect for subtitle
    const subtitle = document.querySelector('.subtitle');
    if (subtitle) {
        const originalText = subtitle.textContent;
        setTimeout(() => {
            typeWriter(subtitle, originalText, 50);
        }, 1000);
    }

    // Add copy to clipboard functionality for installation command
    const codeElements = document.querySelectorAll('code');
    
    codeElements.forEach(code => {
        code.style.cursor = 'pointer';
        code.title = 'Click to copy';
        
        code.addEventListener('click', function() {
            const text = this.textContent;
            navigator.clipboard.writeText(text).then(() => {
                const originalText = this.textContent;
                this.textContent = 'Copied!';
                this.style.background = '#7CB342';
                this.style.color = 'white';
                
                setTimeout(() => {
                    this.textContent = originalText;
                    this.style.background = '';
                    this.style.color = '';
                }, 1000);
            });
        });
    });

    // Add scroll-to-top button
    const scrollTopBtn = document.createElement('button');
    scrollTopBtn.innerHTML = '↑';
    scrollTopBtn.className = 'scroll-top';
    scrollTopBtn.style.cssText = `
        position: fixed;
        bottom: 20px;
        right: 20px;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background: var(--primary-color);
        color: white;
        border: none;
        font-size: 1.5rem;
        cursor: pointer;
        opacity: 0;
        transition: opacity 0.3s ease;
        z-index: 1000;
    `;
    
    document.body.appendChild(scrollTopBtn);
    
    scrollTopBtn.addEventListener('click', () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
    
    window.addEventListener('scroll', () => {
        if (window.pageYOffset > 300) {
            scrollTopBtn.style.opacity = '1';
            scrollTopBtn.style.visibility = 'visible';
            scrollTopBtn.style.transform = 'translateY(0)';
        } else {
            scrollTopBtn.style.opacity = '0';
            scrollTopBtn.style.visibility = 'hidden';
            scrollTopBtn.style.transform = 'translateY(20px)';
        }
    });
});

// Retro effect functions
function createMatrixBackground() {
    // Create subtle matrix-style moving dots for background
    const canvas = document.createElement('canvas');
    canvas.style.position = 'fixed';
    canvas.style.top = '0';
    canvas.style.left = '0';
    canvas.style.width = '100%';
    canvas.style.height = '100%';
    canvas.style.pointerEvents = 'none';
    canvas.style.zIndex = '-1';
    canvas.style.opacity = '0.1';
    
    document.body.appendChild(canvas);
    
    const ctx = canvas.getContext('2d');
    
    function resizeCanvas() {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
    }
    
    resizeCanvas();
    window.addEventListener('resize', resizeCanvas);
    
    const particles = [];
    const particleCount = 30;
    
    for (let i = 0; i < particleCount; i++) {
        particles.push({
            x: Math.random() * canvas.width,
            y: Math.random() * canvas.height,
            vx: (Math.random() - 0.5) * 0.5,
            vy: (Math.random() - 0.5) * 0.5,
            size: Math.random() * 2 + 1
        });
    }
    
    function animate() {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.fillStyle = '#39ff14';
        
        particles.forEach(particle => {
            particle.x += particle.vx;
            particle.y += particle.vy;
            
            if (particle.x < 0 || particle.x > canvas.width) particle.vx *= -1;
            if (particle.y < 0 || particle.y > canvas.height) particle.vy *= -1;
            
            ctx.beginPath();
            ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
            ctx.fill();
        });
        
        requestAnimationFrame(animate);
    }
    
    animate();
}

function addGlitchEffect(element) {
    element.style.animation = 'glitch 0.3s ease-out';
    setTimeout(() => {
        element.style.animation = '';
    }, 300);
}

function addRetroClickEffect(element, event) {
    const rect = element.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    
    const ripple = document.createElement('div');
    ripple.style.cssText = `
        position: absolute;
        left: ${x}px;
        top: ${y}px;
        width: 0;
        height: 0;
        background: rgba(57, 255, 20, 0.6);
        border-radius: 50%;
        transform: translate(-50%, -50%);
        animation: retro-ripple 0.6s ease-out;
        pointer-events: none;
        z-index: 1000;
    `;
    
    element.style.position = 'relative';
    element.appendChild(ripple);
    
    setTimeout(() => {
        ripple.remove();
    }, 600);
}

// Add CSS animations for retro effects
const retroStyles = document.createElement('style');
retroStyles.textContent = `
    @keyframes glitch {
        0%, 100% { transform: translateX(0); }
        20% { transform: translateX(-2px); }
        40% { transform: translateX(2px); }
        60% { transform: translateX(-1px); }
        80% { transform: translateX(1px); }
    }
    
    @keyframes retro-ripple {
        to {
            width: 200px;
            height: 200px;
            opacity: 0;
        }
    }
    
    .loading-dots::after {
        content: '';
        animation: loading-dots 1.5s infinite;
    }
    
    @keyframes loading-dots {
        0%, 20% { content: ''; }
        40% { content: '.'; }
        60% { content: '..'; }
        80%, 100% { content: '...'; }
    }
`;

document.head.appendChild(retroStyles);
