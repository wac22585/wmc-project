<template>
    <v-container class="login-form">
        <div class="login-div">
            <div class="title">USER-MANAGEMENT</div>
            <div class="subtitle">sign in</div>
            <div class="subtitle">to continue</div>

            <v-form @submit.prevent="login">
                <div class="form">
                    <v-text-body1>Email address</v-text-body1>
                    <div>
                        <InputField
                        :model-value="email" @update:model-value="newValue => email = newValue" inputType="text"></InputField>
                        
                    </div>
                    <v-text-body1>Password</v-text-body1>
                    <div>
                        <InputField 
                        :model-value="password" @update:model-value="newValue => password = newValue" inputType="password"></InputField>
                    </div>
                    <div v-if="invalidLogin" class="error-message">{{ invalidLogin }}</div>
                </div>
                <div class="btn-div">
                    <Btn type="submit" label="Continue"/>
                </div>
            </v-form>
        </div>
    </v-container>
</template>

<script>
    import axios from 'axios';
    import InputField from '@/components/Input.vue';
    import Btn from '@/components/Button.vue';
    import { useRouter } from 'vue-router';

    export default {
        data() {
            return {
                email: '',
                password: '',
                invalidLogin: '',
            };
        },
        components: {
            InputField,
            Btn,
        },
        methods: {
        async login() {
            try {
                this.invalidLogin = '';
                const email = this.email;
                const password = this.password;
                console.log(email, password);
                const response = await axios.post('https://localhost:8443/api/auth/login', {
                    username: email,
                    password: password
                }, {
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    withCredentials: true
                });
                if(response.status === 200) {
                    localStorage.setItem('email', response.data.email);
                   this.$router.push({name: 'Home'});
                } else {
                    alert('Login failed. Please check your credentials');
                }
            } catch (error) {
                console.log(error)
                this.invalidLogin = error.response?.data || 'Login failed. Please try again.';
            }
        },
    },
};
</script>

<style scoped>
    .login-form {
        vertical-align: middle;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100vh;
    }

    .login-div {
        width: 400px;
        margin: auto;
    }

    .btn-div {
        display: flex;
        justify-content: end;
    }

    .title {
        color: black;
        font-size: 30px;
    }

    .subtitle {
        color: #707070;
        margin: 0px 0px 5px 0px;
    }

    .form {
        margin-top: 20px;
        margin-bottom: 10px;
    }

    .error-message {
        color: red;
        font-size: 12px;
        margin-top: 5px;
    }
</style>