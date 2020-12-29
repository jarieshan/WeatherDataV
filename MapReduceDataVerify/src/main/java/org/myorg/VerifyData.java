package org.myorg;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.BasicConfigurator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * MapReduce数据清洗
 */
public class VerifyData {

    public static String verifyData(String[] words) {

        if (words[7].equals("-")) {
            words[7] = "\\N";
        }

        return StringUtils.join(words, ",");
    }

    /**
     * 自定义Mapper继承：org.apache.hadoop.mapreduce.Mapper，实现map方法
     */
    public static class WordCountMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            String[] words = value.toString().split(",");
            String text = verifyData(words);
            context.write(new Text(text), NullWritable.get());

        }
    }


    public static void main(String[] args) throws Exception {

        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(d);

        BasicConfigurator.configure();
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setJarByClass(VerifyData.class);

        job.setNumReduceTasks(0);

        job.setMapperClass(WordCountMapper.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]+"/qweather/"+dateNowStr));  // 每日执行一次
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}