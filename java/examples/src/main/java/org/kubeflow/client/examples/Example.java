package org.kubeflow.client.examples;

import io.kubernetes.client.models.*;
import java.io.IOException;
import java.util.*;
import org.kubeflow.client.ApiClient;
import org.kubeflow.client.ApiException;
import org.kubeflow.client.Configuration;
import org.kubeflow.client.apis.KubeflowOrgV1alpha2Api;
import org.kubeflow.client.models.V1alpha2TFJob;
import org.kubeflow.client.models.V1alpha2TFJobSpec;
import org.kubeflow.client.models.V1alpha2TFReplicaSpec;
import org.kubeflow.client.util.Config;

/**
 * A simple example of how to use the Java API
 *
 * <p>
 *
 * <p>Easiest way to run this: mvn exec:java -Dexec.mainClass="org.kubeflow.client.examples.Example"
 *
 * <p>
 *
 * <p>From inside $REPO_DIR/examples
 */
public class Example {
  public static void main(String[] args) throws IOException, ApiException {
    ApiClient client = Config.defaultClient();
    Configuration.setDefaultApiClient(client);

    KubeflowOrgV1alpha2Api api = new KubeflowOrgV1alpha2Api();

    V1alpha2TFJob job =
        new V1alpha2TFJob()
            .apiVersion("kubeflow.org/v1alpha2")
            .kind("TFJob")
            .metadata(new V1ObjectMeta().name("test"));
    V1alpha2TFJobSpec spec = new V1alpha2TFJobSpec().cleanPodPolicy("Running");

    Map<String, V1alpha2TFReplicaSpec> replicas = new HashMap<>();

    // PS Spec
    V1alpha2TFReplicaSpec psSpec =
        new V1alpha2TFReplicaSpec().replicas(2).restartPolicy("OnFailure");

    V1PodTemplateSpec psTemplateSpec = new V1PodTemplateSpec();
    V1PodSpec psPodSpec = new V1PodSpec();

    V1Container psContainer =
        new V1Container()
            .name("tensorflow")
            .image("registry.cn-hangzhou.aliyuncs.com/jetmuffin/tensorflow:1.7.0-hdfs");
    psContainer.setCommand(Arrays.asList("kfrun", "/app/dist-mnist.py"));
    psContainer.addVolumeMountsItem(new V1VolumeMount().mountPath("/app").name("code1"));
    psContainer.addVolumeMountsItem(
        new V1VolumeMount().mountPath("/tmp/mnist-data").name("volume1"));
    psPodSpec.addContainersItem(psContainer);

    psPodSpec.addVolumesItem(
        new V1Volume()
            .name("code1")
            .hostPath(new V1HostPathVolumeSource().path("/tmp/mnist_code")));
    psPodSpec.addVolumesItem(
        new V1Volume()
            .name("volume1")
            .hostPath(new V1HostPathVolumeSource().path("/tmp/mnist_data")));

    psTemplateSpec.setSpec(psPodSpec);
    psSpec.setTemplate(psTemplateSpec);

    // Worker Spec
    V1alpha2TFReplicaSpec workerSpec =
        new V1alpha2TFReplicaSpec().replicas(2).restartPolicy("OnFailure");
    V1PodTemplateSpec workerTemplateSpec = new V1PodTemplateSpec();
    V1PodSpec workerPodSpec = new V1PodSpec();

    V1Container workerContainer =
        new V1Container()
            .name("tensorflow")
            .image("registry.cn-hangzhou.aliyuncs.com/jetmuffin/tensorflow:1.7.0-hdfs");
    workerContainer.setCommand(Arrays.asList("kfrun", "/app/dist-mnist.py"));
    workerContainer.addVolumeMountsItem(new V1VolumeMount().mountPath("/app").name("code2"));
    workerContainer.addVolumeMountsItem(
        new V1VolumeMount().mountPath("/tmp/mnist-data").name("volume2"));
    workerPodSpec.addContainersItem(workerContainer);

    workerPodSpec.addVolumesItem(
        new V1Volume()
            .name("code2")
            .hostPath(new V1HostPathVolumeSource().path("/tmp/mnist_code")));
    workerPodSpec.addVolumesItem(
        new V1Volume()
            .name("volume2")
            .hostPath(new V1HostPathVolumeSource().path("/tmp/mnist_data")));

    workerTemplateSpec.setSpec(workerPodSpec);
    workerSpec.setTemplate(psTemplateSpec);

    replicas.put("PS", psSpec);
    replicas.put("worker", workerSpec);

    spec.setTfReplicaSpecs(replicas);
    job.setSpec(spec);

    V1alpha2TFJob result = api.createNamespacedTFJob("default", job, "true");
    System.out.println(result.getMetadata().getName());
  }
}
