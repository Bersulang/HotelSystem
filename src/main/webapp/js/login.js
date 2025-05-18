// 登录页面交互效果
document.addEventListener('DOMContentLoaded', function() {
    // 密码可见性切换
    const togglePassword = document.getElementById('togglePassword');
    if (togglePassword) {
        togglePassword.addEventListener('click', function() {
            const passwordInput = document.getElementById('password');
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            this.textContent = type === 'password' ? '👁️' : '👁️‍🗨️';
        });
    }
    
    // 输入框动画效果
    const inputs = document.querySelectorAll('.input-field');
    inputs.forEach(input => {
        // 初始化时检查输入框是否已有值
        if (input.value) {
            input.parentElement.classList.add('has-value');
        }
        
        // 焦点效果
        input.addEventListener('focus', function() {
            this.parentElement.classList.add('focused');
            animateInput(this, true);
        });
        
        // 失去焦点效果
        input.addEventListener('blur', function() {
            if (!this.value) {
                this.parentElement.classList.remove('focused');
            }
            this.parentElement.classList.toggle('has-value', !!this.value);
            animateInput(this, false);
        });
        
        // 输入时的键盘效果
        input.addEventListener('input', function() {
            if (this.value) {
                addRippleEffect(this);
            }
        });
    });
    
    // 输入框的动画函数
    function animateInput(element, isFocus) {
        if (isFocus) {
            element.style.transform = 'translateY(-2px)';
            element.style.boxShadow = '0 4px 8px rgba(66, 133, 244, 0.2)';
        } else {
            element.style.transform = 'translateY(0)';
            element.style.boxShadow = 'none';
        }
    }
    
    // 添加涟漪效果
    function addRippleEffect(element) {
        const ripple = document.createElement('span');
        ripple.classList.add('ripple');
        
        // 随机位置
        const size = Math.max(element.offsetWidth, element.offsetHeight);
        const x = (Math.random() * element.offsetWidth) - (size / 2);
        const y = (Math.random() * 10) - (size / 2);
        
        ripple.style.width = ripple.style.height = `${size/10}px`;
        ripple.style.left = `${x}px`;
        ripple.style.top = `${y}px`;
        
        element.appendChild(ripple);
        
        setTimeout(() => {
            ripple.remove();
        }, 800);
    }
    
    // 登录按钮效果
    const loginButton = document.querySelector('.submit-btn');
    if (loginButton) {
        loginButton.addEventListener('mousedown', function() {
            this.style.transform = 'scale(0.98)';
        });
        
        loginButton.addEventListener('mouseup', function() {
            this.style.transform = 'translateY(-2px)';
        });
        
        loginButton.addEventListener('mouseleave', function() {
            this.style.transform = '';
        });
    }
    
    // 自动隐藏提示消息
    const messages = document.querySelectorAll('.message');
    if (messages.length > 0) {
        setTimeout(() => {
            messages.forEach(msg => {
                msg.style.opacity = '0';
                msg.style.height = '0';
                msg.style.padding = '0';
                msg.style.margin = '0';
                msg.style.overflow = 'hidden';
                msg.style.transition = 'all 0.5s ease';
            });
        }, 5000);
    }
    
    // 背景动画效果
    createParticles();
});

// 创建背景粒子效果
function createParticles() {    const authContainer = document.querySelector('.auth-container');
    if (!authContainer) return;
    
    // 创建粒子容器
    const particlesContainer = document.createElement('div');
    particlesContainer.className = 'particles-container';
    document.body.insertBefore(particlesContainer, document.body.firstChild);
    
    // 添加样式
    const style = document.createElement('style');
    style.textContent = `
        .particles-container {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            pointer-events: none;
            z-index: 0;
        }
        
        .particle {
            position: absolute;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.5);
            pointer-events: none;
            z-index: 0;
        }
        
        .ripple {
            position: absolute;
            background: rgba(66, 133, 244, 0.3);
            border-radius: 50%;
            transform: scale(0);
            animation: rippleEffect 0.8s ease-out;
            pointer-events: none;
        }
        
        @keyframes rippleEffect {
            to {
                transform: scale(2);
                opacity: 0;
            }
        }
    `;
    document.head.appendChild(style);
    
    // 创建粒子
    for (let i = 0; i < 50; i++) {
        createParticle();
    }
    
    function createParticle() {
        const particle = document.createElement('div');
        particle.className = 'particle';
        
        // 随机尺寸和位置
        const size = Math.random() * 5 + 1;
        let x = Math.random() * window.innerWidth;
        let y = Math.random() * window.innerHeight;
        
        // 随机透明度和速度
        const opacity = Math.random() * 0.5 + 0.1;
        const speedX = (Math.random() - 0.5) * 1;
        const speedY = (Math.random() - 0.5) * 1;
        
        // 应用样式
        particle.style.width = `${size}px`;
        particle.style.height = `${size}px`;
        particle.style.left = `${x}px`;
        particle.style.top = `${y}px`;
        particle.style.opacity = opacity;
        
        particlesContainer.appendChild(particle);
        
        // 移动粒子
        function moveParticle() {
            x += speedX;
            y += speedY;
            
            // 边界检查
            if (x < 0 || x > window.innerWidth) {
                x = Math.random() * window.innerWidth;
            }
            
            if (y < 0 || y > window.innerHeight) {
                y = Math.random() * window.innerHeight;
            }
            
            particle.style.left = `${x}px`;
            particle.style.top = `${y}px`;
            
            requestAnimationFrame(moveParticle);
        }
        
        moveParticle();
    }
}
