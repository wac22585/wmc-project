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

                const response = await axios.get('/users/login', {
                    params: {
                        email: email,
                        password: encodeURIComponent(password)
                    }
                });

                if(response.status === 200 && response.data) {
                    console.log('Login successfull', response.data);
                    this.$router.push({name: 'Home'});

                    localStorage.setItem('authToken', response.data.token);
                } else {
                    console.log('Login failed');
                    alert('Login failed. Please check your credentials');
                }
            } catch (error) {
                if (error.response && error.response.status === 401) {
                    this.invalidLogin = 'Invalid email or password';
                } else {
                    this.invalidLogin = 'An error occurred.';
                }
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

    .btn-div {
        display: flex;
        justify-content: end;
    }

    .error-message {
        color: red;
        font-size: 12px;
        margin-top: 5px;
    }
</style>