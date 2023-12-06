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
                        <InputField v-model="email" inputType="text"></InputField>
                    </div>
                    <v-text-body1>Password</v-text-body1>
                    <div>
                        <InputField v-model="password" inputType="password"></InputField>
                    </div>
                </div>
                <div class="btn-div">
                    <Btn type="submit" label="Continue"/>
                </div>
            </v-form>
        </div>
    </v-container>
</template>

<script setup>
    import axios from 'axios';
    import InputField from '@/components/Input.vue';
    import Btn from '@/components/Button.vue';
    import { useRouter } from 'vue-router';

</script>

<script>
    export default {
        data() {
            return {
            email: '',
            password: '',
            };
        },
        components: {
            InputField,
            Btn,
        },
        methods: {
            async login() {
            try {
                const response = true; // Replace this with your actual login logic

                if (response) {
                console.log('Login successful');

                // Redirect to the home page
                this.$router.push({ name: 'Home' });
                } else {
                console.error('Login failed');
                alert('Login failed. Please check your credentials.');
                }
            } catch (error) {
                console.error('Login error', error);
                alert('An error occurred during login. Please try again later.');
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
</style>