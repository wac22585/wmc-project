<template>
    <label for="">Date of Birth</label>
    <InputField id="dob" v-model="formattedDate" @click="openDatePicker" @focus="openDatePicker" readonly="true" class="inputfield" />
    <v-dialog width="300" class="date-picker" v-model="showDatePicker" :return-value.sync="dob" persistent>
        <v-date-picker v-model="dob" :max="maxDate" @input="dateSelected">
        <template v-slot:actions>
            <v-spacer></v-spacer>
            <v-btn text color="black" @click="closeDatePicker">Close</v-btn>
        </template>
        </v-date-picker>
    </v-dialog>
</template>

<script setup>
    import InputField from '@/components/Input.vue';
</script>

<script>
    export default {
        components: {
            InputField
        },
        data() {
            return {
                showDatePicker: false,
                dob: new Date(),
            }
        },
        computed: {
            formattedDate() {
                return this.dob instanceof Date ? this.dob.toLocaleDateString() : '';
            },
            maxDate() {
                let today = new Date();
                var dd = today.getDate();
                var mm = today.getMonth()+1; 
                var yyyy = today.getFullYear();
                today = yyyy + '-' + mm + '-' + dd;
                console.log(today)
                return today;
            }
        },
        methods: {
            openDatePicker() {
                this.showDatePicker = true;
            },
            closeDatePicker() {
                this.showDatePicker = false;
            },
        }
    }
</script>